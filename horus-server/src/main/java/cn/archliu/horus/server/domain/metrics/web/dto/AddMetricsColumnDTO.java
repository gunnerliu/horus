package cn.archliu.horus.server.domain.metrics.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel("添加指标列")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class AddMetricsColumnDTO {

    @ApiModelProperty("指标 code")
    @NotBlank(message = "指标 code 不允许为空")
    private String metricsCode;

    @ApiModelProperty("列名")
    @NotBlank(message = "列名不允许为空")
    private String columnName;

    @ApiModelProperty("列 code")
    @NotBlank(message = "列 code不允许为空")
    private String columnCode;

    @ApiModelProperty("列类型")
    @NotNull(message = "列类型不允许为空")
    private String columnType;

}
