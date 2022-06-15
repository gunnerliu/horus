package cn.archliu.horus.server.domain.reach.enums;

/**
 * @Author: Arch
 * @Date: 2022-05-28 22:35:46
 * @Description: 消息发送状态
 */
public enum SendState {

    SUCCESS, // 发送成功
    FAIL, // 发送失败
    SILENT, // 静默的，降噪处理重复的消息，不会进行发送，但是会记录
    PENDING // 挂起，待聚合后发送

}
