package cn.archliu.horus.infr.domain.metrics.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 指标数据结构
 * </p>
 *
 * @author Arch
 * @since 2022-04-27
 */
@Data
@Accessors(chain = true)
@TableName("horus_metrics_desc")
public class HorusMetricsDesc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 指标 code
     */
    private String metricsCode;

    /**
     * 指标名
     */
    private String metricsName;

    /**
     * 指标类型,COUNTER->计数,METRICS->指标
     */
    private String metricsType;

    /**
     * TDEngine超级表的表名
     */
    private String taosStName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
