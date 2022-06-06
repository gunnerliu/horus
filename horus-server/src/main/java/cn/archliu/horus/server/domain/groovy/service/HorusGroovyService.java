package cn.archliu.horus.server.domain.groovy.service;

import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;

/**
 * @Author: Arch
 * @Date: 2022-05-20 14:25:34
 * @Description: groovy 脚本相关服务
 *               集群场景下需要进行改造: 1、将groovy脚本存放文件放入一个分布式的文件管理中心; 2、锁不需要改造
 */
public interface HorusGroovyService {

    /**
     * 执行 groovy 脚本
     * 
     * @param groovyCode
     * @param args
     * @return
     */
    Object executeGroovy(String groovyCode, Map<String, Object> args);

    /**
     * 新增 groovy 脚本
     * 
     * @param groovyInfo
     */
    void addGroovy(HorusGroovyInfo groovyInfo);

    /**
     * 卸载 groovy 脚本
     * 
     * @param groovyCode
     */
    void unInstallGroovy(String groovyCode);

    /**
     * 删除 groovy 脚本
     * 
     * @param groovyCode
     */
    void delGroovy(String groovyCode);

    /**
     * 查询所有 groovy 脚本缓存
     * 
     * @return
     */
    Set<String> loadGroovyCache();

    /**
     * 查看 groovy 脚本详情
     * 
     * @param groovyCode
     * @return
     */
    String groovyDetail(String groovyCode);

    /**
     * 分页查询 groovy 脚本信息
     * 
     * @param page
     * @return
     */
    IPage<HorusGroovyInfo> pageQuery(Page<HorusGroovyInfo> page);

}
