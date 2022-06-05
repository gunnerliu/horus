package cn.archliu.horus.server.domain.collection.filter;

import cn.archliu.horus.infr.domain.collection.entity.MetricsCol;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;

/**
 * @Author: Arch
 * @Date: 2022-05-16 11:20:46
 * @Description: metrics 数据落地之前会进行一些处理,子类需要托管给 Spring
 */
public interface HorusMetricsFilter {

    /** 处理所有的 metric */
    public static final String ALL = "AllMetric";

    /**
     * metrics 数据处理
     * 
     * @param metricsDesc
     * @param metric
     * @param appName
     * @param accessParty
     *                    不进行落地的话可以抛出异常
     */
    void process(HorusMetricsDesc metricsDesc, MetricsCol metric, String appName, String accessParty);

    /**
     * 只处理某个 metricsCode
     * 默认处理所有的 metricsCode
     * 
     * @return
     */
    default String filterMetricsCode() {
        return ALL;
    }

}
