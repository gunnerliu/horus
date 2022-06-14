package cn.archliu.horus.server.domain.reach.web.dto;

import javax.validation.constraints.NotBlank;

import cn.archliu.horus.common.enums.ComState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("触达通道")
@Data
@Accessors(chain = true)
public class ReachChannelDTO {

    @ApiModelProperty("通道编码")
    @NotBlank(message = "通道编码不允许为空！")
    private String channelCode;

    @ApiModelProperty("通道名称")
    @NotBlank(message = "通道名称不允许为空！")
    private String channelName;

    @ApiModelProperty("匹配类别编码")
    @NotBlank(message = "匹配类别编码不允许为空！")
    private String categoryCode;

    @ApiModelProperty("启用状态")
    private ComState enableState = ComState.ENABLED;

}
