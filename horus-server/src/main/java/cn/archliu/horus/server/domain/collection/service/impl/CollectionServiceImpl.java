package cn.archliu.horus.server.domain.collection.service.impl;

import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.ACCESS_PARTY;
import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.APP_NAME;
import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.INSTANCE_ID;
import static cn.archliu.horus.server.domain.groovy.enums.ScriptParamName.METRICS;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.archliu.common.exception.sub.ParamErrorException;
import cn.archliu.horus.infr.domain.collection.entity.HorusColGroovyFilter;
import cn.archliu.horus.infr.domain.collection.entity.HorusCounter;
import cn.archliu.horus.infr.domain.collection.entity.MetricsCol;
import cn.archliu.horus.infr.domain.collection.mapper.HorusColGroovyFilterMapper;
import cn.archliu.horus.infr.domain.collection.mapper.HorusMetricsMapper;
import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.infr.domain.groovy.mapper.HorusGroovyInfoMapper;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import cn.archliu.horus.server.domain.collection.filter.HorusMetricsFilter;
import cn.archliu.horus.server.domain.collection.service.CollectionService;
import cn.archliu.horus.server.domain.groovy.event.GroovyAddEvent;
import cn.archliu.horus.server.domain.groovy.event.GroovyDelEvent;
import cn.archliu.horus.server.domain.groovy.service.HorusGroovyService;
import cn.archliu.horus.server.domain.metrics.enums.MetricsType;
import cn.archliu.horus.server.domain.metrics.service.MetricsCodeCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CollectionServiceImpl implements CollectionService, ApplicationRunner {

    @Autowired
    private MetricsCodeCache metricsCodeCache;

    @Autowired
    private HorusMetricsMapper metricsMapper;

    @Autowired(required = false)
    private List<HorusMetricsFilter> horusMetricsFilters;

    /** groovy 脚本数据处理 filter, key->metricsCode,value->List(groovyCode) */
    private Map<String, Set<String>> groovyFilters = new HashMap<>();

    @Autowired
    private HorusGroovyService horusGroovyService;

    @Autowired
    private HorusGroovyInfoMapper groovyInfoMapper;

    @Autowired
    private HorusColGroovyFilterMapper colGroovyFilterMapper;

    @Override
    public void counterCol(List<HorusCounter> counters, String appName, String accessParty, String instanceId) {
        String splitTag = processSplitTag(appName, instanceId);
        String childTableName = "horus_counter_st_" + genInstance(instanceId);
        metricsMapper.counterCol(counters, appName, accessParty, splitTag, childTableName);
    }

    @Override
    public void metricsCol(List<MetricsCol> metrics, String appName, String accessParty, String instanceId) {
        for (MetricsCol metric : metrics) {
            try {
                // metricsCode 单个处理
                metricsItemCol(metric, appName, accessParty, instanceId);
            } catch (Exception e) {
                log.error("metricsCode: [{}] col error", metric.getMetricsCode(), e);
            }
        }
    }

    /**
     * metricsCode 单个处理
     * 
     * @param metric
     * @param appName
     * @param accessParty
     * @param instanceId
     */
    @SuppressWarnings({ "squid:S3776" })
    private void metricsItemCol(MetricsCol metric, String appName, String accessParty, String instanceId) {
        // 获取 metricsCode 的数据结构
        HorusMetricsDesc metricsDesc = metricsCodeCache.loadMetricsCode(metric.getMetricsCode());
        if (metricsDesc == null || !StrUtil.equals(MetricsType.METRICS.name(), metricsDesc.getMetricsType())) {
            log.warn("指标code: {} 不存在", metric.getMetricsCode());
            return;
        }
        // 捞出超级表的表名
        metric.setTaosStName(metricsDesc.getTaosStName());
        // 将字段名进行驼峰转换
        List<String> collect = metric.getMetricsColumns().stream()
                .map(columnName -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, columnName))
                .collect(Collectors.toList());
        metric.setMetricsColumns(collect);
        // splitTag 处理
        processSplitTag(metric, appName, instanceId);
        // 添加数据自定义处理 filter
        if (CollUtil.isNotEmpty(horusMetricsFilters)) {
            List<HorusMetricsFilter> metricsFilters = horusMetricsFilters.stream()
                    .filter(item -> HorusMetricsFilter.ALL.equals(item.filterMetricsCode())
                            || StrUtil.equals(metric.getMetricsCode(), item.filterMetricsCode()))
                    .collect(Collectors.toList());
            for (HorusMetricsFilter filter : metricsFilters) {
                try {
                    filter.process(metricsDesc, metric, appName, accessParty);
                } catch (Exception e) {
                    log.error("filter process error, interrupt [{}] col !", metric.getMetricsCode(), e);
                    return;
                }
            }
        }
        Set<String> groovyCodes = groovyFilters.entrySet().stream()
                .filter(item -> item.getKey().equals(HorusMetricsFilter.ALL)
                        || item.getKey().equals(metric.getMetricsCode()))
                .map(Entry::getValue).flatMap(Collection::stream).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(groovyCodes)) {
            for (String groovyCode : groovyCodes) {
                try {
                    Map<String, Object> param = new HashMap<>();
                    param.put(METRICS, metric);
                    param.put(APP_NAME, appName);
                    param.put(ACCESS_PARTY, accessParty);
                    param.put(INSTANCE_ID, instanceId);
                    horusGroovyService.executeGroovy(groovyCode, param);
                } catch (Exception e) {
                    log.error("groovy filter process error, interrupt [{}] col !", metric.getMetricsCode(), e);
                    return;
                }
            }
        }

        // 指标数据落地
        metricsMapper.metricsCol(metric, appName, accessParty);
    }

    /**
     * 处理 splitTag,splitTag影响到数据的写入，不同的实力需要使用不同的 splitTag，不然会出现数据覆盖的问题
     * 
     * @param metric
     * @param appName
     * @param instanceId
     */
    private void processSplitTag(MetricsCol metric, String appName, String instanceId) {
        if (StrUtil.isNotBlank(metric.getSplitTag())) {
            String sName = null;
            if (StrUtil.isNotBlank(appName) || StrUtil.isNotBlank(instanceId)) {
                sName = StrUtil.removePrefix(appName + "_" + genInstance(instanceId), "_");
            } else {
                sName = "basic";
            }
            metric.setSplitTag(StrUtil.replace(sName, "-", "_"));
            metric.setChildTableName(metric.getTaosStName() + "_" + genInstance(instanceId));
        }
    }

    /**
     * 处理 splitTag,splitTag影响到数据的写入，不同的实例需要使用不同的 splitTag，不然会出现数据覆盖的问题
     * 
     * @param appName
     * @param instanceId
     * @return
     */
    private String processSplitTag(String appName, String instanceId) {
        if (StrUtil.isNotBlank(appName) || StrUtil.isNotBlank(instanceId)) {
            return StrUtil.removePrefix(
                    StrUtil.replace(appName + "_" + genInstance(instanceId), "-", "_"), "_");
        } else {
            return "basic_counter";
        }
    }

    private static String genInstance(String instanceId) {
        return StrUtil.isBlank(instanceId) ? "" : StrUtil.replace(StrUtil.replace(instanceId, ".", "_"), ":", "_");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refreshGroovyFilters();
    }

    /** 30 秒刷新 1 次 */
    @Scheduled(fixedDelay = 30000)
    protected void scheduleRefreshGroovyFilters() {
        refreshGroovyFilters();
    }

    // 监听 groovy 脚本删除、新增的事件,分布式改造需要通过mq广播,
    // 这里会有个问题，，groovy 脚本删除后，这里执行会报错，导致数据落地失败，但删 groovy 脚本的操作会很少，这里就不做处理了
    @EventListener({ GroovyDelEvent.class, GroovyAddEvent.class })
    @Override
    public void refreshGroovyFilters() {
        List<HorusColGroovyFilter> filters = colGroovyFilterMapper.selectList(null);
        Map<String, Set<String>> newGroovyFilters = new HashMap<>();
        if (CollUtil.isNotEmpty(filters)) {
            for (HorusColGroovyFilter filter : filters) {
                if (newGroovyFilters.containsKey(filter.getMetricsCode())) {
                    newGroovyFilters.get(filter.getMetricsCode()).add(filter.getGroovyCode());
                } else {
                    Set<String> groovyCodeSet = new HashSet<>();
                    groovyCodeSet.add(filter.getGroovyCode());
                    newGroovyFilters.put(filter.getMetricsCode(), groovyCodeSet);
                }
            }
        }
        groovyFilters = newGroovyFilters;
    }

    @Override
    public void addGroovyFilter(String metricsCode, String groovyCode) {
        // 校验下 metricsCode 是否存在
        HorusMetricsDesc loadMetricsCode = metricsCodeCache.loadMetricsCode(metricsCode);
        if (loadMetricsCode == null) {
            throw ParamErrorException.throwE("metricsCode: " + metricsCode + " 不存在！");
        }
        // 校验下 groovyCode 是否存在
        boolean groovyExists = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                .eq(HorusGroovyInfo::getGroovyCode, groovyCode).exists();
        if (!groovyExists) {
            throw ParamErrorException.throwE("groovyCode: " + groovyCode + " 不存在！");
        }
        // 检查写这个 metricsCode 、groovyCode 的 filter 是否已经已经存在
        boolean filterExists = new LambdaQueryChainWrapper<>(colGroovyFilterMapper)
                .eq(HorusColGroovyFilter::getMetricsCode, metricsCode)
                .eq(HorusColGroovyFilter::getGroovyCode, groovyCode).exists();
        if (filterExists) {
            throw ParamErrorException
                    .throwE("metricsCode: " + metricsCode + ",groovyCode: " + groovyCode + " 的 filter 已存在！");
        }
        colGroovyFilterMapper.insert(new HorusColGroovyFilter().setMetricsCode(metricsCode).setGroovyCode(groovyCode));
    }

    @Override
    public void removeGroovyFilter(Long id) {
        colGroovyFilterMapper.deleteById(id);
    }

    @Override
    public IPage<HorusColGroovyFilter> pageGroovyFilter(Page<HorusColGroovyFilter> page) {
        return colGroovyFilterMapper.selectPage(page, null);
    }

}
