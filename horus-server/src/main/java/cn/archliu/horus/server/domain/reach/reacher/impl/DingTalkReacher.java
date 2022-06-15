package cn.archliu.horus.server.domain.reach.reacher.impl;

import org.springframework.stereotype.Component;

import cn.archliu.horus.common.exception.sub.MessagePushException;
import cn.archliu.horus.server.domain.reach.entity.DingTalkMessage;
import cn.archliu.horus.server.domain.reach.entity.DingTalkMessage.At;
import cn.archliu.horus.server.domain.reach.entity.DingTalkMessage.Markdown;
import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.entity.ReceiverInfo;
import cn.archliu.horus.server.domain.reach.reacher.HorusReacher;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

/**
 * @Author: Arch
 * @Date: 2022-05-28 22:00:16
 * @Description: 钉钉消息触达
 */
@Component("dingTalkReacher")
public class DingTalkReacher implements HorusReacher {

    @Override
    public void sendMessage(HorusMessage message, ReceiverInfo receiver) {
        String content = genSendContent(message, receiver.getAtMobiles());
        DingTalkMessage sendMsg = new DingTalkMessage()
                .setMarkdown(new Markdown().setTitle("Horus Message").setText(content))
                .setAt(new At().setAtMobiles(receiver.getAtMobiles()));
        String res = HttpUtil.post(receiver.getWebHook(), JSONUtil.toJsonStr(sendMsg));
        if (StrUtil.isBlank(res) || !res.contains("\"errcode\":0")) {
            throw MessagePushException.throwE(res);
        }
    }

}
