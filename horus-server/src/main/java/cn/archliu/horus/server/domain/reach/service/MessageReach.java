package cn.archliu.horus.server.domain.reach.service;

import cn.archliu.horus.server.domain.reach.entity.HorusMessage;

/**
 * @Author: Arch
 * @Date: 2022-05-28 17:20:01
 * @Description: 消息触达服务
 */
public interface MessageReach {

    /**
     * 消息推送，异步
     * 
     * @param message
     */
    void sendMessage(HorusMessage message);

    /**
     * 聚合消息推送, 30 秒聚合推送一次
     */
    void aggregationMessagePush();

}
