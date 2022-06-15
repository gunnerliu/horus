package cn.archliu.horus.server.domain.collection.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.archliu.horus.infr.domain.collection.entity.HorusColGroovyFilter;
import cn.archliu.horus.infr.domain.collection.entity.HorusCounter;
import cn.archliu.horus.infr.domain.collection.entity.MetricsCol;

/**
 * @Author: Arch
 * @Date: 2022-05-01 22:22:26
 * @Description: 数据采集相关服务
 */
public interface CollectionService {

    /**
     * 计数指标数据采集
     * 
     * @param counters
     * @param appName
     * @param accessParty
     * @param instanceId
     */
    void counterCol(List<HorusCounter> counters, String appName, String accessParty, String instanceId);

    /**
     * 实体指标数据采集
     * 
     * @param metrics
     * @param appName
     * @param accessParty
     * @param instanceId
     */
    void metricsCol(List<MetricsCol> metrics, String appName, String accessParty, String instanceId);

    /**
     * 刷新 groovy filter 脚本
     */
    void refreshGroovyFilters();

    /**
     * 添加 GroovyFilter
     * 
     * @param metricsCode
     * @param groovyCode
     */
    void addGroovyFilter(String metricsCode, String groovyCode);

    /**
     * 删除 GroovyFilter
     * 
     * @param id
     */
    void removeGroovyFilter(Long id);

    /**
     * 分页查询 groovy filter 信息
     * 
     * @param page
     * @return
     */
    IPage<HorusColGroovyFilter> pageGroovyFilter(Page<HorusColGroovyFilter> page);

}
