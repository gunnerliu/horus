package cn.archliu.horus.server.domain.schedule.task;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

import cn.archliu.horus.common.enums.ComState;
import cn.archliu.horus.common.exception.sub.ParamErrorException;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleHistory;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.infr.domain.schedule.mapper.HorusScheduleHistoryMapper;
import cn.archliu.horus.infr.domain.schedule.mapper.HorusScheduleJobMapper;
import cn.archliu.horus.server.domain.groovy.enums.ScriptParamName;
import cn.archliu.horus.server.domain.groovy.service.HorusGroovyService;
import cn.archliu.horus.server.domain.schedule.enums.ScheduleJobType;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-04-24 15:04:38
 * @Description: 定时任务线程
 */
@Slf4j
public class ScheduledTaskRunnable implements Runnable {

    private String jobCode;

    private ScheduleJobType jobType;

    public ScheduledTaskRunnable(String jobCode, String jobType) {
        this.jobCode = jobCode;
        this.jobType = ScheduleJobType.convertThrowE(jobType);
    }

    @Override
    public void run() {
        HorusScheduleJobMapper scheduleMapper = SpringUtil.getBean(HorusScheduleJobMapper.class);
        HorusScheduleJob job = new LambdaQueryChainWrapper<>(scheduleMapper)
                .eq(HorusScheduleJob::getJobCode, jobCode)
                .eq(HorusScheduleJob::getState, ComState.ENABLED.name()).one();
        if (job == null) {
            log.warn("定时任务 jobCode: {} 已删除或已停用！", jobCode);
            return;
        }
        HorusScheduleHistory history = new HorusScheduleHistory().setJobCode(job.getJobCode())
                .setCornStr(job.getCornStr()).setJobExecuteId(job.getJobExecuteId()).setJobType(job.getJobType())
                .setParamStr(job.getParamStr()).setExecuteState("SUCCESS");
        try {
            // 校验 execute_job_id 是否存在
            switch (jobType) {
                case BEAN:
                    executeBean(job);
                    break;
                case SCRIPT:
                    executeScript(job);
                    break;
                default:
                    throw ParamErrorException.throwE("定时任务类型错误！");
            }
        } catch (Exception e) {
            history.setExecuteState("FAIL").setMsg(e.getMessage());
        }
        // 添加定时任务的执行记录
        HorusScheduleHistoryMapper scheduleHistoryMapper = SpringUtil.getBean(HorusScheduleHistoryMapper.class);
        scheduleHistoryMapper.insert(history);
        // TODO 定时任务执行失败的进行告警
    }

    /**
     * 执行 Spring Bean
     */
    private void executeBean(HorusScheduleJob job) {
        HorusScheduleTask<?> task = SpringUtil.getBean(job.getJobExecuteId(), HorusScheduleTask.class);
        task.run(jobCode);
    }

    /**
     * 执行脚本
     */
    private void executeScript(HorusScheduleJob job) {
        Map<String, Object> params = new HashMap<>();
        // 定时任务参数
        if (StrUtil.isNotBlank(job.getParamStr())) {
            params.put(ScriptParamName.HORUS_SCHEDULE_PARAM, JSONUtil.parseArray(job.getParamStr()));
        }
        HorusGroovyService horusGroovyService = SpringUtil.getBean(HorusGroovyService.class);
        // 参数赋值
        log.info("定时任务开始执行-jobCode: {}, 参数: {}", jobCode, job.getParamStr() == null ? "空" : job.getParamStr());
        horusGroovyService.executeGroovy(job.getJobExecuteId(), params);
    }

}
