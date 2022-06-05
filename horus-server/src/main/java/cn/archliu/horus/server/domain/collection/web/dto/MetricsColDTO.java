package cn.archliu.horus.server.domain.collection.web.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("实体指标采集实体")
@Data
@Accessors(chain = true)
public class MetricsColDTO {

    @ApiModelProperty("实体指标 code")
    @NotBlank(message = "指标code不允许为空")
    private String metricsCode;

    @ApiModelProperty("指标字段")
    @NotEmpty(message = "指标字段不允许为空")
    private List<String> metricsColumns;

    @ApiModelProperty("指标字段值")
    @NotEmpty(message = "指标字段值不允许为空")
    private List<List<Object>> metricsColumnsValue;

    @ApiModelProperty("分隔标签")
    private String splitTag = "basic";

}
