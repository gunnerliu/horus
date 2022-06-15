package cn.archliu.horus.server.domain.schedule.enums;

import cn.archliu.horus.common.exception.sub.ParamErrorException;

/**
 * @Author: Arch
 * @Date: 2022-04-24 22:35:06
 * @Description: 定时任务类型
 */
public enum ScheduleJobType {

    BEAN, // 定时执行 Spring Bean
    SCRIPT; // 定时执行脚本

    public static ScheduleJobType convertThrowE(String scheduleJobType) {
        for (ScheduleJobType item : ScheduleJobType.values()) {
            if (item.name().equals(scheduleJobType)) {
                return item;
            }
        }
        throw ParamErrorException.throwE("定时任务类型错误！");
    }
}
