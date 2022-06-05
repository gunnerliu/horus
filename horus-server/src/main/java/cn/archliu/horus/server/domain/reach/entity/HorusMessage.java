package cn.archliu.horus.server.domain.reach.entity;

import cn.archliu.horus.server.domain.reach.enums.Level;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-28 17:21:02
 * @Description: 需要发送的信息
 */
@Data
@Accessors(chain = true)
public class HorusMessage {

    /** 消息类别 */
    private String categoryCode;

    /** 消息标签 */
    private String tag;

    /** 消息等级，默认为 WARNING */
    private Level level = Level.AGGREGATION;

    /** 消息内容参数 */
    private String content;

}
