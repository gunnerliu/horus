package cn.archliu.horus.server.domain.reach.web.dto;

import javax.validation.constraints.NotBlank;

import cn.archliu.horus.server.domain.reach.enums.Level;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("发送消息请求")
@Data
@Accessors(chain = true)
public class HorusMessageDTO {

    @ApiModelProperty("消息类别")
    private String categoryCode;

    @ApiModelProperty("消息标签")
    private String tag;

    @ApiModelProperty("消息等级，默认为 WARNING")
    private Level level = Level.AGGREGATION;

    @ApiModelProperty("消息内容")
    @NotBlank(message = "消息内容不允许为空")
    private String content;

}
