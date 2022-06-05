package cn.archliu.horus.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Author: Arch
 * @Date: 2022-04-24 11:22:55
 * @Description: 定时任务线程池配置
 */
@Configuration
public class ScheduleConfig {

    @Bean("horusTaskSchedule")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler horusTaskSchedule = new ThreadPoolTaskScheduler();
        horusTaskSchedule.setPoolSize(5);
        horusTaskSchedule.setRemoveOnCancelPolicy(true);
        horusTaskSchedule.setThreadNamePrefix("HorusTaskSchedulerThreadPool-");
        return horusTaskSchedule;
    }

}
