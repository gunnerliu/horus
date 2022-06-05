package cn.archliu.horus.common.exception.sub;

import cn.archliu.horus.common.exception.BaseException;

/**
 * @Author: Arch
 * @Date: 2022-05-28 21:39:14
 * @Description: 消息推送异常
 */
public class MessagePushException extends BaseException {

    public MessagePushException() {
        super("message push exception !");
    }

    public MessagePushException(String message) {
        super(message);
    }

    public static MessagePushException throwE() {
        return new MessagePushException();
    }

    public static MessagePushException throwE(String message) {
        return new MessagePushException(message);
    }

}
