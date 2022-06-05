package cn.archliu.horus.server.domain.reach.reacher;

import java.util.List;
import java.util.stream.Collectors;

import cn.archliu.common.exception.sub.ParamErrorException;
import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.entity.ReceiverInfo;
import cn.archliu.horus.server.domain.reach.enums.ReacherType;
import cn.archliu.horus.server.domain.reach.reacher.impl.DingTalkReacher;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;

/**
 * @Author: Arch
 * @Date: 2022-05-28 13:58:46
 * @Description: 消息触达器
 */
public interface HorusReacher {

    /**
     * 发送消息
     * 
     * @param message
     * @param receiver
     */
    void sendMessage(HorusMessage message, ReceiverInfo receiver);

    @SuppressWarnings({ "all" })
    public static HorusReacher getReacher(ReacherType reacherType) {
        switch (reacherType) {
            case DING_TALK:
                return SpringUtil.getBean("dingTalkReacher", DingTalkReacher.class);
        }
        throw ParamErrorException.throwE("该触达器暂不支持");
    }

    /**
     * 构建消息内容
     * 
     * @param message
     * @param atMobiles
     * @return
     */
    public default String genSendContent(HorusMessage message, List<String> atMobiles) {
        String atMob = "";
        if (CollUtil.isNotEmpty(atMobiles)) {
            List<String> atMobs = atMobiles.stream().map(item -> "@" + item).collect(Collectors.toList());
            atMob = String.join(",", atMobs);
        }
        return new StringBuilder().append("# 触达通道： ").append(message.getCategoryCode())
                .append("\n").append("## 消息标签： ").append(message.getTag()).append("\n").append("### 通知人员： ")
                .append(atMob).append("\n >").append(message.getContent()).append("\n\n-- Horus").toString();
    }

}
