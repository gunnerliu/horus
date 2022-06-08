package cn.archliu.horus.server.domain.schedule.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.archliu.horus.common.enums.ComState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-06-08 18:15:16
 * @Description: 修改定时任务状态
 */
@ApiModel("修改定时任务状态")
@Data
@Accessors(chain = true)
public class EditScheduleStateDTO {

    @ApiModelProperty("定时任务 code")
    @NotBlank(message = "定时任务 code 不允许为空")
    private String jobCode;

    @ApiModelProperty("定时任务状态")
    @NotNull(message = "定时任务状态不允许为空")
    private ComState state;

}
