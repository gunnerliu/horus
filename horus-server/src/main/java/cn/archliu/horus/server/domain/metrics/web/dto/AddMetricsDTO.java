package cn.archliu.horus.server.domain.metrics.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.archliu.horus.server.domain.metrics.enums.MetricsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("添加指标")
@Data
@Accessors(chain = true)
public class AddMetricsDTO {

    @ApiModelProperty("指标 code")
    @NotBlank(message = "指标 code 不允许为空！")
    private String metricsCode;

    @ApiModelProperty("指标名")
    private String metricsName;

    @ApiModelProperty("指标类型,COUNTER->计数,METRICS->指标")
    @NotNull(message = "指标类型不允许为空！")
    private MetricsType metricsType;

}
