package cn.archliu.horus.server.domain.schedule.task;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import cn.archliu.horus.common.enums.ComState;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.infr.domain.schedule.mapper.HorusScheduleJobMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-04-24 22:28:42
 * @Description: 定时任务注册器
 */
@Slf4j
@Component
public class CronTaskRegister {

    /** key -> jobCode, */
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>(16);

    @Resource(name = "horusTaskSchedule")
    private TaskScheduler taskScheduler;

    @Autowired
    private HorusScheduleJobMapper jobMapper;

    /**
     * 添加定时任务
     * 
     * @param jobCode
     * @param cronExpression
     * @param jobType
     */
    public synchronized void addCronTask(String jobCode, String cronExpression, String jobType) {
        // 一个 jobCode 只允许一个定时线程
        if (scheduledTasks.containsKey(jobCode)) {
            log.warn("定时任务线程已存在,jobCode: {}", jobCode);
            return;
        }
        // 创建执行定时任务的线程
        ScheduledTaskRunnable taskRunnable = new ScheduledTaskRunnable(jobCode);
        ScheduledFuture<?> schedule = taskScheduler.schedule(taskRunnable,
                new CronTask(taskRunnable, cronExpression).getTrigger());
        // 放入到缓存中
        scheduledTasks.put(jobCode, schedule);
        log.info("启动定时任务 - [{}] - [{}] - [{}]", jobCode, cronExpression, jobType);
    }

    /**
     * 移除定时任务
     * 
     * @param jobCode
     */
    public synchronized void removeCronTask(String jobCode) {
        if (!scheduledTasks.containsKey(jobCode)) {
            log.info("定时任务: {} 已移除!", jobCode);
            return;
        }
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(jobCode);
        if (!scheduledFuture.isCancelled()) {
            // 取消定时任务
            scheduledFuture.cancel(true);
        }
        // 移除缓存
        scheduledTasks.remove(jobCode);
    }

    /**
     * 同步数据中的定时任务
     */
    @Scheduled(fixedDelay = 5000L)
    protected void syncDB() {
        // 查询出数据库中启用的定时任务
        List<HorusScheduleJob> dbJobs = new LambdaQueryChainWrapper<>(jobMapper)
                .eq(HorusScheduleJob::getState, ComState.ENABLED.name()).list();
        // 数据库中无启用的定时任务
        if (CollUtil.isEmpty(dbJobs)) {
            // 清除所有的定时任务
            clearCronJobs();
            return;
        }
        // 内存中的定时任务与 DB 中进行比较
        List<String> collect = dbJobs.stream().map(HorusScheduleJob::getJobCode).collect(Collectors.toList());
        if (MapUtil.isNotEmpty(scheduledTasks)) {
            Iterator<Entry<String, ScheduledFuture<?>>> iterator = scheduledTasks.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, ScheduledFuture<?>> next = iterator.next();
                // DB 中不存在，需要移除
                if (!collect.contains(next.getKey())) {
                    if (!next.getValue().isCancelled()) {
                        next.getValue().cancel(true);
                    }
                    scheduledTasks.remove(next.getKey());
                } else {
                    collect.remove(next.getKey());
                }
            }
        }
        // collect 中剩下的就是新增的
        List<HorusScheduleJob> newJobs = dbJobs.stream().filter(job -> collect.contains(job.getJobCode()))
                .collect(Collectors.toList());
        // 新增定时任务
        if (CollUtil.isNotEmpty(newJobs)) {
            for (HorusScheduleJob job : newJobs) {
                addCronTask(job.getJobCode(), job.getCornStr(), job.getJobType());
            }
        }
    }

    /**
     * 清除所有的定时任务
     */
    private void clearCronJobs() {
        if (MapUtil.isEmpty(scheduledTasks)) {
            return;
        }
        Iterator<Entry<String, ScheduledFuture<?>>> iterator = scheduledTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, ScheduledFuture<?>> next = iterator.next();
            // 取消执行
            if (!next.getValue().isCancelled()) {
                // 取消定时任务
                next.getValue().cancel(true);
            }
            // 移除缓存
            scheduledTasks.remove(next.getKey());
        }
    }

}
