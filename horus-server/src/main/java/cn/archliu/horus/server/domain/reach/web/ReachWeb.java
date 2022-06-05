package cn.archliu.horus.server.domain.reach.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.server.domain.reach.service.MessageReach;
import cn.archliu.horus.server.domain.reach.web.convert.MessageConvert;
import cn.archliu.horus.server.domain.reach.web.dto.HorusMessageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@Api(tags = { "消息触达接口,内网(/inner/*)接口不做鉴权" })
@RestController
@RequestMapping({ "/inner/horus/reach", "/api/horus/reach" })
public class ReachWeb {

    @Autowired
    private MessageReach messageReach;

    @ApiOperation("发送消息")
    @PutMapping("/sendMessage")
    public ComRes<ResData<Void>> sendMessage(@RequestBody HorusMessageDTO message) {
        messageReach.sendMessage(MessageConvert.INSTANCE.trans(message));
        return ComRes.success();
    }

}
