package cn.archliu.horus.server.domain.reach.enums;

/**
 * @Author: Arch
 * @Date: 2022-05-28 19:06:39
 * @Description: 消息等级
 */
public enum Level {

    INSTANT, // 即时消息，消息不进行合并，即时推送
    AGGREGATION // 聚合消息，消息合并推送，不展示内容

}
