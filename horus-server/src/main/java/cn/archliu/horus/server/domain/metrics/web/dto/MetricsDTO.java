package cn.archliu.horus.server.domain.metrics.web.dto;

import java.time.LocalDateTime;

import cn.archliu.horus.server.domain.metrics.enums.MetricsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("指标实体")
@Data
@Accessors(chain = true)
public class MetricsDTO {

    @ApiModelProperty("指标 code")
    private String metricsCode;

    @ApiModelProperty("TDEngine超级表的表名")
    private String taosStName;

    @ApiModelProperty("指标名")
    private String metricsName;

    @ApiModelProperty("指标类型,COUNTER->计数,METRICS->指标")
    private MetricsType metricsType;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
