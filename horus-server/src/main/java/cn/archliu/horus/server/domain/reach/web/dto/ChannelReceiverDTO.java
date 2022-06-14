package cn.archliu.horus.server.domain.reach.web.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("通道添加接收人")
@Data
@Accessors(chain = true)
public class ChannelReceiverDTO {

    @ApiModelProperty("通道ID")
    @NotNull(message = "通达ID不允许为空")
    private Long channelId;

    @ApiModelProperty("消息接收人ID")
    @NotNull(message = "消息接收人ID不允许为空")
    private Long receiverId;

}
