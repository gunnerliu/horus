package cn.archliu.horus.infr.domain.metrics.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 指标列信息
 * </p>
 *
 * @author Arch
 * @since 2022-04-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_metrics_columns")
public class HorusMetricsColumns implements Serializable {

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
     * 列名
     */
    private String columnName;

    /**
     * 列 code
     */
    private String columnCode;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
