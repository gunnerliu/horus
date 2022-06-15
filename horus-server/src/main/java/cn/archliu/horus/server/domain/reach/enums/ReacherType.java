package cn.archliu.horus.server.domain.reach.enums;

import cn.archliu.common.exception.sub.ParamErrorException;

/**
 * @Author: Arch
 * @Date: 2022-05-28 22:16:22
 * @Description: 触达器类型
 */
public enum ReacherType {

    DING_TALK, // 钉钉
    WORK_WX, // 企业微信
    FEI_SHU, // 飞书
    EMAIL; // 电子邮件

    public static ReacherType transThrowE(String reacher) {
        for (ReacherType item : values()) {
            if (item.name().equals(reacher)) {
                return item;
            }
        }
        throw ParamErrorException.throwE("reacher 类型错误, " + reacher);
    }

}
