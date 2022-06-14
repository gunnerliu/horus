package cn.archliu.horus.server.domain.reach.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.archliu.horus.common.enums.ComState;
import cn.archliu.horus.server.domain.reach.enums.ReacherType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("触达接收者")
@Data
@Accessors(chain = true)
public class ReachReceiverDTO {

    @ApiModelProperty("接收人名称")
    @NotBlank(message = "接收人名称不允许为空！")
    private String receiverName;

    @ApiModelProperty("消息推送方式,dingtalk->钉钉,workwx->企业微信,feishu->飞书,email->电子邮箱")
    @NotNull(message = "消息推送方式不允许为空！")
    private ReacherType reacher;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("电子邮箱")
    private String email;

    @ApiModelProperty("群机器人webhook")
    private String webHook;

    @ApiModelProperty("启用状态")
    private ComState enableState = ComState.ENABLED;

}
