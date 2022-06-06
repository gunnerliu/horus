package cn.archliu.horus.server.domain.groovy.service.impl;

import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.LOG;
import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.MASTER;
import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.MESSAGE_REACH;
import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.TD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import cn.archliu.common.exception.sub.ParamErrorException;
import cn.archliu.horus.common.exception.sub.GroovyExecuteException;
import cn.archliu.horus.common.exception.sub.IoException;
import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.infr.domain.groovy.mapper.HorusGroovyInfoMapper;
import cn.archliu.horus.server.config.HorusServerProperties;
import cn.archliu.horus.server.domain.groovy.entity.GroovyObjectCache;
import cn.archliu.horus.server.domain.groovy.entity.GroovyScriptEngineCache;
import cn.archliu.horus.server.domain.groovy.enums.ExecuteType;
import cn.archliu.horus.server.domain.groovy.event.GroovyDelEvent;
import cn.archliu.horus.server.domain.groovy.service.HorusGroovyService;
import cn.archliu.horus.server.domain.reach.service.MessageReach;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import lombok.extern.slf4j.Slf4j;

@DependsOn("flywayInitializer")
@Slf4j
@Service
public class HorusGroovyServiceImpl implements HorusGroovyService {

    private static final String GROOVY_PATH = "classpath:groovy/";

    /** groovy 脚本缓存, key->groovyCode, value->脚本缓存对象 */
    private Map<String, GroovyObjectCache> groovyClassCache = new ConcurrentHashMap<>();

    /** groovy 脚本缓存, key->groovyCode, value->脚本缓存对象 */
    private Map<String, GroovyScriptEngineCache> groovyScriptCache = new ConcurrentHashMap<>();

    /** 根据 groovyCode 需要加读写锁，保证脚本缓存的更新 */
    private Map<String, ReentrantReadWriteLock> groovyLocks = new ConcurrentHashMap<>();

    @Autowired
    private DynamicRoutingDataSource dataSource;

    @Autowired
    private HorusServerProperties horusServerProperties;

    @Autowired
    private HorusGroovyInfoMapper groovyInfoMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MessageReach messageReach;

    /**
     * 初始化 groovy 脚本
     * 
     * @throws Exception
     */
    @PostConstruct
    public void initScripts() throws Exception {
        // 同步文件目录下的脚本信息到数据库,集群环境需要加分布式锁
        syncGroovyInfo();
        // 捞出所有需要加载的脚本
        List<HorusGroovyInfo> groovyInfos = groovyInfoMapper.selectList(null);
        if (CollUtil.isEmpty(groovyInfos)) {
            log.info("无 groovy 脚本初始化！");
            return;
        }
        List<HorusGroovyInfo> classCollect = groovyInfos.stream()
                .filter(item -> ExecuteType.CLASS_LOAD.name().equals(item.getExecuteType()))
                .collect(Collectors.toList());
        List<HorusGroovyInfo> scriptCollect = groovyInfos.stream()
                .filter(item -> ExecuteType.SCRIPT.name().equals(item.getExecuteType()))
                .collect(Collectors.toList());

        // 添加 class 缓存
        addGroovyClassCache(classCollect);
        // 添加 script 缓存
        addGroovyScriptCache(scriptCollect);
    }

    /**
     * 同步文件目录下的脚本信息到数据库,集群环境需要加分布式锁
     * 
     * @throws FileNotFoundException
     */
    @SuppressWarnings({ "squid:S3776" })
    private void syncGroovyInfo() throws FileNotFoundException {
        // 同步 resources/groovy 目录下的脚本信息
        if (BooleanUtil.isTrue(horusServerProperties.getSyncResourcesGroovy())) {
            File file = new File(ResourceUtils.getFile(GROOVY_PATH).getPath());
            File[] listFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(listFiles)) {
                List<String> collect = ListUtil.toList(listFiles).stream().map(f -> GROOVY_PATH + f.getName())
                        .collect(Collectors.toList());
                // 筛选出未同步的文件信息
                List<HorusGroovyInfo> existed = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                        .in(HorusGroovyInfo::getFilePath, collect).list();
                List<String> existedFile = existed.stream().map(HorusGroovyInfo::getFilePath)
                        .collect(Collectors.toList());
                List<File> newFile = ListUtil.toList(listFiles).stream()
                        .filter(item -> !existedFile.contains(GROOVY_PATH + item.getName()))
                        .collect(Collectors.toList());
                for (File f : newFile) {
                    HorusGroovyInfo newInfo = new HorusGroovyInfo().setLastModTime(file.lastModified())
                            .setGroovyCode(IdUtil.simpleUUID()).setFilePath(GROOVY_PATH + f.getName())
                            .setExecuteType(ExecuteType.SCRIPT.name());
                    try {
                        BufferedReader reader = FileUtil.getReader(f, StandardCharsets.UTF_8);
                        String firstLine = reader.readLine();
                        if (StrUtil.isNotBlank(firstLine)
                                && StrUtil.containsIgnoreCase(firstLine, ExecuteType.CLASS_LOAD.name())) {
                            newInfo.setExecuteType(ExecuteType.CLASS_LOAD.name());
                        }
                    } catch (Exception e) {
                        log.error("读取 groovy 脚本首行异常！", e);
                    }
                    groovyInfoMapper.insert(newInfo);
                }
            }
        }
    }

    /**
     * @Author: Arch
     * @Date: 2022-05-20 22:46:04
     * @Description: 添加 class 缓存
     */
    @SuppressWarnings({ "squid:S112", "squid:S2095" })
    private void addGroovyClassCache(List<HorusGroovyInfo> classCollect) throws Exception {
        if (CollUtil.isEmpty(classCollect)) {
            return;
        }
        for (HorusGroovyInfo groovyInfo : classCollect) {
            // 在进行 groovy 脚本更新的时候需要将 groovyClassLoader 进行 close
            GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
            // 这边分为两种，一种是resources目录下的，另一种是存放在数据库中的
            Class<?> parseClass = null;
            long lastModified = groovyInfo.getLastModTime();
            if ("DB".equals(groovyInfo.getFilePath())) {
                parseClass = groovyClassLoader.parseClass(groovyInfo.getScriptContent());
            } else {
                File file = new File(ResourceUtils.getURL(groovyInfo.getFilePath()).getPath());
                parseClass = groovyClassLoader.parseClass(file);
                lastModified = file.lastModified();
            }
            GroovyObject groovyObject = (GroovyObject) parseClass.newInstance();
            // 塞入数据源
            groovyObject.setProperty(TD, dataSource.getDataSource("td"));
            groovyObject.setProperty(MASTER, dataSource.getDataSource("master"));
            groovyObject.setProperty(LOG, LoggerFactory.getLogger(groovyInfo.getGroovyCode()));
            groovyObject.setProperty(MESSAGE_REACH, messageReach);
            groovyClassCache.put(groovyInfo.getGroovyCode(),
                    new GroovyObjectCache().setGroovyClassLoader(groovyClassLoader).setGroovyObject(groovyObject)
                            .setLastModTime(groovyInfo.getLastModTime()));
            // 更新表中的 lastModTime
            new LambdaUpdateChainWrapper<>(groovyInfoMapper).set(HorusGroovyInfo::getLastModTime, lastModified)
                    .eq(HorusGroovyInfo::getGroovyCode, groovyInfo.getGroovyCode()).update();
        }
    }

    /**
     * @Author: Arch
     * @Date: 2022-05-20 22:46:16
     * @Description: 添加 script 缓存
     */
    @SuppressWarnings({ "squid:S112", "squid:S2095" })
    private void addGroovyScriptCache(List<HorusGroovyInfo> scriptCollect) throws Exception {
        if (CollUtil.isEmpty(scriptCollect)) {
            return;
        }
        for (HorusGroovyInfo groovyInfo : scriptCollect) {
            // 在进行 groovy 脚本更新的时候需要将 groovyClassLoader 进行 close
            GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
            long lastModified = groovyInfo.getLastModTime();
            // 参数赋值
            Binding binding = new Binding();
            // 塞入数据源
            binding.setProperty(TD, dataSource.getDataSource("td"));
            binding.setProperty(MASTER, dataSource.getDataSource("master"));
            binding.setProperty(LOG, LoggerFactory.getLogger(groovyInfo.getGroovyCode()));
            binding.setProperty(MESSAGE_REACH, messageReach);
            Script groovyScript = null;
            if ("DB".equals(groovyInfo.getFilePath())) {
                Class<?> parseClass = groovyClassLoader.parseClass(groovyInfo.getScriptContent(),
                        groovyInfo.getGroovyCode());
                groovyScript = InvokerHelper.createScript(parseClass, binding);
            } else {
                File file = new File(ResourceUtils.getFile(groovyInfo.getFilePath()).getPath());
                lastModified = file.lastModified();
                GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(file.getPath(), groovyClassLoader);
                groovyScript = groovyScriptEngine.createScript(groovyInfo.getGroovyCode(), binding);
            }
            groovyScriptCache.put(groovyInfo.getGroovyCode(),
                    new GroovyScriptEngineCache().setGroovyCode(groovyInfo.getGroovyCode())
                            .setGroovyClassLoader(groovyClassLoader).setGroovyScript(groovyScript)
                            .setLastModTime(groovyInfo.getLastModTime()));
            // 更新表中的 lastModTime
            new LambdaUpdateChainWrapper<>(groovyInfoMapper).set(HorusGroovyInfo::getLastModTime, lastModified)
                    .eq(HorusGroovyInfo::getGroovyCode, groovyInfo.getGroovyCode()).update();
        }
    }

    @Override
    public Object executeGroovy(String groovyCode, Map<String, Object> args) {
        if (StrUtil.isBlank(groovyCode)) {
            throw ParamErrorException.throwE("groovyCode 为空！");
        }
        // 执行 groovy 脚本前的准备
        prepareGroovy(groovyCode);
        // 执行 groovy 脚本
        return processGroovy(groovyCode, args);

    }

    /**
     * 执行 groovy 脚本前的准备
     * 
     * @param groovyCode
     */
    private void prepareGroovy(String groovyCode) {
        // 获取 groovyCode 的读写锁
        ReentrantReadWriteLock lock = getLock(groovyCode);
        // 还未加载过的脚本加写锁
        lock.writeLock().lock();
        try {
            // 加载 groovy 脚本
            HorusGroovyInfo one = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                    .eq(HorusGroovyInfo::getGroovyCode, groovyCode).one();
            if (one == null) {
                log.warn("{} 脚本不存在！", groovyCode);
                throw ParamErrorException.throwE(groovyCode + " 脚本不存在！");
            }
            // 如果缓存中不存在
            if (!groovyClassCache.containsKey(groovyCode) && !groovyScriptCache.containsKey(groovyCode)) {
                if (ExecuteType.CLASS_LOAD.name().equals(one.getExecuteType())) {
                    addGroovyClassCache(ListUtil.toList(one));
                } else if (ExecuteType.SCRIPT.name().equals(one.getExecuteType())) {
                    addGroovyScriptCache(ListUtil.toList(one));
                } else {
                    throw ParamErrorException.throwE("groovy 脚本执行类型错误！");
                }
            } else { // 缓存中如果存在，则检查 groovy 文件是否有更新
                refreshClassCache(one);
                refreshScriptCache(one);
            }
        } catch (Exception e) {
            log.error("加载 groovy 脚本异常, {} ！", groovyCode, e);
            throw GroovyExecuteException.throwE(groovyCode + "脚本加载异常");
        } finally {
            lock.writeLock().unlock();
        }
        // 缓存重新加载之后还是没有则代表未加载到该脚本
        if (!groovyClassCache.containsKey(groovyCode) && !groovyScriptCache.containsKey(groovyCode)) {
            throw ParamErrorException.throwE("该脚本： " + groovyCode + " 未加载到！");
        }
    }

    /**
     * 执行 groovy 脚本
     * 
     * @param groovyCode
     * @param args
     * @return
     */
    private Object processGroovy(String groovyCode, Map<String, Object> args) {
        // 获取 groovyCode 的读写锁
        ReentrantReadWriteLock lock = getLock(groovyCode);
        // 加读锁
        lock.readLock().lock();
        try {
            GroovyObjectCache groovyObjectCache = groovyClassCache.get(groovyCode);
            if (groovyObjectCache != null) {
                // 执行脚本
                return groovyObjectCache.getGroovyObject().invokeMethod("run", args);
            }
            GroovyScriptEngineCache groovyScriptEngineCache = groovyScriptCache.get(groovyCode);
            if (groovyScriptEngineCache != null) {
                try {
                    if (MapUtil.isNotEmpty(args)) {
                        Iterator<Entry<String, Object>> iterator = args.entrySet().iterator();
                        Binding binding = groovyScriptEngineCache.getGroovyScript().getBinding();
                        while (iterator.hasNext()) {
                            Entry<String, Object> next = iterator.next();
                            binding.setProperty(next.getKey(), next.getValue());
                        }
                    }
                    // 执行脚本
                    return groovyScriptEngineCache.getGroovyScript().run();
                } catch (Exception e) {
                    log.error("groovyCode: {} groovy execute error!", groovyCode, e);
                    throw GroovyExecuteException.throwE();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        log.error("未执行到 groovy 脚本,groovyCode: {}", groovyCode);
        throw GroovyExecuteException.throwE("未执行到 groovy 脚本！");
    }

    /**
     * 刷新 groovy 脚本
     * 
     * @param horusGroovyInfo
     * @throws Exception
     */
    private void refreshClassCache(HorusGroovyInfo horusGroovyInfo) throws Exception {
        if (!groovyClassCache.containsKey(horusGroovyInfo.getGroovyCode())) {
            // 缓存中没有，无需刷新
            return;
        }

        if (groovyClassCache.get(horusGroovyInfo.getGroovyCode()).getLastModTime()
                .equals(horusGroovyInfo.getLastModTime())) {
            // 文件未修改，无需刷新
            return;
        }
        try {
            // 清除缓存
            GroovyObjectCache groovyObjectCache = groovyClassCache.get(horusGroovyInfo.getGroovyCode());
            groovyObjectCache.getGroovyClassLoader().close();
            groovyClassCache.remove(horusGroovyInfo.getGroovyCode());
            // 重新加载
            addGroovyClassCache(ListUtil.toList(horusGroovyInfo));
        } catch (IOException e) {
            log.error("关闭 GroovyClassLoader 异常, horusGroovyInfo： {}", horusGroovyInfo.toString(), e);
            throw e;
        }
    }

    /**
     * 刷新 groovy 脚本
     * 
     * @param horusGroovyInfo
     * @throws Exception
     */
    private void refreshScriptCache(HorusGroovyInfo horusGroovyInfo) throws Exception {
        if (!groovyScriptCache.containsKey(horusGroovyInfo.getGroovyCode())) {
            // 缓存中没有，无需刷新
            return;
        }

        // 尽量避免直接在服务器文件上修改，通过前端上传，如果需要实时检测服务器上文件是否修改需要打开 CheckGroovyFileChange 开关
        if (groovyScriptCache.get(horusGroovyInfo.getGroovyCode()).getLastModTime()
                .equals(horusGroovyInfo.getLastModTime())) {
            // 文件未修改，无需刷新
            return;
        }
        try {
            // 清除缓存
            GroovyScriptEngineCache groovyScriptEngineCache = groovyScriptCache.get(horusGroovyInfo.getGroovyCode());
            groovyScriptEngineCache.getGroovyClassLoader().close();
            groovyScriptCache.remove(horusGroovyInfo.getGroovyCode());
            // 重新加载
            addGroovyScriptCache(ListUtil.toList(horusGroovyInfo));
        } catch (IOException e) {
            log.error("关闭 GroovyClassLoader 异常, horusGroovyInfo： {}", horusGroovyInfo.toString(), e);
            throw e;
        }
    }

    /**
     * 根据 groovyCode 获取锁
     * 
     * @param groovyCode
     * @return
     */
    private synchronized ReentrantReadWriteLock getLock(String groovyCode) {
        return groovyLocks.computeIfAbsent(groovyCode, k -> new ReentrantReadWriteLock());
    }

    @Override
    public void addGroovy(HorusGroovyInfo groovyInfo) {
        boolean exists = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                .eq(HorusGroovyInfo::getGroovyCode, groovyInfo.getGroovyCode()).exists();
        if (exists) {
            // 更新
            new LambdaUpdateChainWrapper<>(groovyInfoMapper).set(HorusGroovyInfo::getFilePath, groovyInfo.getFilePath())
                    .set(HorusGroovyInfo::getExecuteType, groovyInfo.getExecuteType())
                    .set(HorusGroovyInfo::getScriptContent, groovyInfo.getScriptContent())
                    .set(HorusGroovyInfo::getLastModTime, groovyInfo.getLastModTime()).update();
        } else {
            // 新增
            groovyInfoMapper.insert(groovyInfo);
        }
    }

    @Override
    public void unInstallGroovy(String groovyCode) {
        if (groovyClassCache.containsKey(groovyCode)) {
            ReentrantReadWriteLock lock = getLock(groovyCode);
            lock.writeLock().lock();
            try {
                GroovyObjectCache groovyObjectCache = groovyClassCache.get(groovyCode);
                groovyObjectCache.getGroovyClassLoader().close();
                groovyClassCache.remove(groovyCode);
            } catch (IOException e) {
                log.error("关闭 GroovyClassLoader 异常, groovyCode: {}", groovyCode, e);
                throw GroovyExecuteException.throwE("groovy 脚本卸载失败！");
            } finally {
                lock.writeLock().unlock();
            }
        }
        if (groovyScriptCache.containsKey(groovyCode)) {
            ReentrantReadWriteLock lock = getLock(groovyCode);
            lock.writeLock().lock();
            try {
                GroovyObjectCache groovyObjectCache = groovyClassCache.get(groovyCode);
                groovyObjectCache.getGroovyClassLoader().close();
                groovyClassCache.remove(groovyCode);
            } catch (IOException e) {
                log.error("关闭 GroovyClassLoader 异常, groovyCode: {}", groovyCode, e);
                throw GroovyExecuteException.throwE("groovy 脚本卸载失败！");
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    @SuppressWarnings({ "squid:S4042" })
    @Override
    public void delGroovy(String groovyCode) {
        HorusGroovyInfo one = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                .eq(HorusGroovyInfo::getGroovyCode, groovyCode).one();
        if (one == null) {
            log.info("{} 不存在！", groovyCode);
            return;
        }
        // 删除文件
        try {
            File file = ResourceUtils.getFile(one.getFilePath());
            if (file.exists() && file.isFile() && file.delete()) {
                log.info("{} 已删除！", one.getFilePath());
            }
        } catch (FileNotFoundException e) {
            log.error("脚本未找到！", e);
            throw IoException.throwE("脚本删除失败！");
        }
        // 删除记录
        groovyInfoMapper.deleteById(one.getId());
        // 卸载缓存
        unInstallGroovy(groovyCode);
        // 发布一个删除事件
        applicationEventPublisher.publishEvent(new GroovyDelEvent(groovyCode));
    }

    @Override
    public IPage<HorusGroovyInfo> pageQuery(Page<HorusGroovyInfo> page) {
        return groovyInfoMapper.selectPage(page, null);
    }

    @Override
    public Set<String> loadGroovyCache() {
        return Stream.of(groovyClassCache, groovyScriptCache).flatMap(map -> groovyScriptCache.keySet().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public String groovyDetail(String groovyCode) {
        HorusGroovyInfo groovyInfo = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                .eq(HorusGroovyInfo::getGroovyCode, groovyCode).one();
        if (groovyInfo == null) {
            throw ParamErrorException.throwE("该脚本不存在！");
        }
        if ("DB".equals(groovyInfo.getFilePath())) {
            return groovyInfo.getScriptContent();
        } else {
            try {
                File file = new File(ResourceUtils.getFile(groovyInfo.getFilePath()).getPath());
                FileReader filerReader = FileReader.create(file);
                List<String> readLines = filerReader.readLines();
                StringBuilder sBuilder = new StringBuilder();
                for (String line : readLines) {
                    sBuilder.append(line).append("\n");
                }
                return sBuilder.toString();
            } catch (FileNotFoundException e) {
                log.warn("groovy 脚本未查询到： {}", groovyCode, e);
                throw ParamErrorException.throwE("该脚本不存在！");
            }
        }

    }
}
