package cn.archliu.horus.infr.domain.metrics.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsColumns;

/**
 * <p>
 * 指标列信息 Mapper 接口
 * </p>
 *
 * @author Arch
 * @since 2022-04-27
 */
public interface HorusMetricsColumnsMapper extends BaseMapper<HorusMetricsColumns> {

    /**
     * 初始表里的默认字段
     * 
     * @param metricsCode
     */
    void initColumns(@Param("metricsCode") String metricsCode);

    /**
     * 新增列
     * 
     * @param horusMetricsColumns
     */
    void batchInsert(@Param("metricsColumns") List<HorusMetricsColumns> metricsColumns);

}
