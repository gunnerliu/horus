package cn.archliu.horus.server.domain.reach.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-28 14:12:57
 * @Description: 钉钉消息
 */
@Data
@Accessors(chain = true)
public class DingTalkMessage {

    /** 消息类型 */
    private String msgtype = "markdown";

    /** markdown 消息 */
    private Markdown markdown;

    /** at 成员 */
    private At at;

    @Data
    @Accessors(chain = true)
    public static class Markdown {

        private String title;

        private String text;

    }

    @Data
    @Accessors(chain = true)
    public static class At {

        /** 需要 at 成员的手机号 */
        private List<String> atMobiles;

        /** 是否 at 全员 */
        private Boolean isAtAll = Boolean.FALSE;

    }

}
