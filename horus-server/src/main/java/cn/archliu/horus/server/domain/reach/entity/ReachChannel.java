package cn.archliu.horus.server.domain.reach.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-28 18:23:32
 * @Description: 触达通道
 */
@Data
@Accessors(chain = true)
public class ReachChannel {

    /** 触达通道 code */
    private String channelCode;

    /** 消息接收人 */
    private List<ReceiverInfo> receivers;

}
