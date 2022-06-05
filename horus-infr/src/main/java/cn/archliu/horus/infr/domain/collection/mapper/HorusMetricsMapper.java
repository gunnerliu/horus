package cn.archliu.horus.infr.domain.collection.mapper;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;

import org.apache.ibatis.annotations.Param;

import cn.archliu.horus.infr.domain.collection.entity.HorusCounter;
import cn.archliu.horus.infr.domain.collection.entity.MetricsCol;

@DS("td")
public interface HorusMetricsMapper {

    /**
     * 计数指标采集
     * 
     * @param counters
     * @param appName
     * @param accessParty
     * @param splitTag
     */
    void counterCol(@Param("counters") List<HorusCounter> counters, @Param("appName") String appName,
            @Param("accessParty") String accessParty, @Param("splitTag") String splitTag,
            @Param("childTableName") String childTableName);

    /**
     * 指标数据采集
     * 
     * @param metric
     * @param appName
     * @param accessParty
     */
    void metricsCol(@Param("metric") MetricsCol metric, @Param("appName") String appName,
            @Param("accessParty") String accessParty);

}
