package cn.archliu.horus.server.domain.schedule.web.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.archliu.horus.common.enums.ComState;
import cn.archliu.horus.server.domain.schedule.enums.ScheduleJobType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("定时任务参数")
public class ScheduleJobDTO {

    @ApiModelProperty("定时任务编号")
    @NotBlank(message = "定时任务编号不允许为空")
    private String jobCode;

    @ApiModelProperty("定时任务名称")
    @NotBlank(message = "定时任务名称不允许为空")
    private String jobName;

    @ApiModelProperty("corn表达式")
    @NotBlank(message = "corn表达式不允许为空")
    private String cornStr;

    @ApiModelProperty("定时任务类型,BEAN->执行job bean,SCRIPT->执行脚本")
    @NotNull(message = "定时任务类型不允许为空")
    private ScheduleJobType jobType;

    @ApiModelProperty("BEAN->执行 bean,SCRIPT->执行脚本")
    private String jobExecuteId;

    @ApiModelProperty("定时任务参数,json字符串")
    private String paramStr;

    @ApiModelProperty("状态,ENABLED->启用,DISABLED->禁用")
    private ComState state = ComState.ENABLED;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
