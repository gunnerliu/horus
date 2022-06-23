package cn.archliu.horus.server.domain.schedule.web.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("修改定时任务")
@Data
@Accessors(chain = true)
public class EditScheduleDTO {

    @ApiModelProperty("定时任务编号")
    @NotBlank(message = "定时任务编号不允许为空")
    private String jobCode;

    @ApiModelProperty("定时任务名称")
    @NotBlank(message = "定时任务名称不允许为空")
    private String jobName;

    @ApiModelProperty("corn表达式")
    @NotBlank(message = "corn表达式不允许为空")
    private String cornStr;

    @ApiModelProperty("定时任务参数,json字符串")
    private String paramStr;

}
