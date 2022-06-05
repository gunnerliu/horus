package cn.archliu.horus.server.domain.metrics.web.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("指标列")
@Data
@Accessors(chain = true)
public class MetricsColumnDTO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("指标 code")
    private String metricsCode;

    @ApiModelProperty("列名")
    private String columnName;

    @ApiModelProperty("列 code")
    private String columnCode;

    @ApiModelProperty("列类型")
    private String columnType;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
