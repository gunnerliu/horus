package cn.archliu.horus.server.domain.collection.web.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("计数打点采集数据")
@Data
@Accessors(chain = true)
public class HorusCounterDTO {

    @ApiModelProperty("打点时间")
    @NotBlank(message = "打点时间不允许为空")
    private Timestamp markingTime;

    @ApiModelProperty("计数维度 code")
    @NotBlank(message = "计数维度 code 不允许为空")
    private String counterCode;

    @ApiModelProperty("全局链路ID")
    private String traceId;

    @ApiModelProperty("链路节点ID")
    private String spanId;

    @ApiModelProperty("标签")
    private String label;

}
