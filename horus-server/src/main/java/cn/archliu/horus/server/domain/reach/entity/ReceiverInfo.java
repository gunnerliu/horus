package cn.archliu.horus.server.domain.reach.entity;

import java.util.List;

import cn.archliu.horus.server.domain.reach.enums.ReacherType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-28 18:31:34
 * @Description: 信息接收人
 */
@Data
@Accessors(chain = true)
public class ReceiverInfo {

    /** 通知人ID */
    private Long receiverId;

    /** 触达器类型 */
    private ReacherType reacherType;

    private String webHook;

    /** at 的手机号 */
    private List<String> atMobiles;

}
