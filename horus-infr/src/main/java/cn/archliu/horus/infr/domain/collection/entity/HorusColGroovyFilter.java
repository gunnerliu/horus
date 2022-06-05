package cn.archliu.horus.infr.domain.collection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 指标落地前数据处理groovy脚本信息
 * </p>
 *
 * @author Arch
 * @since 2022-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_col_groovy_filter")
public class HorusColGroovyFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * groovy脚本code
     */
    private String groovyCode;

    /**
     * 需要处理的 metricsCode,AllMetric->处理所有的
     */
    private String metricsCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
