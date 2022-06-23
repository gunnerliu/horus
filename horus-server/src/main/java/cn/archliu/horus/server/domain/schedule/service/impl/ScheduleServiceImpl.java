package cn.archliu.horus.server.domain.schedule.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.archliu.horus.common.enums.ComState;
import cn.archliu.horus.common.exception.sub.ParamErrorException;
import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.infr.domain.groovy.mapper.HorusGroovyInfoMapper;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleHistory;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.infr.domain.schedule.mapper.HorusScheduleHistoryMapper;
import cn.archliu.horus.infr.domain.schedule.mapper.HorusScheduleJobMapper;
import cn.archliu.horus.server.domain.schedule.service.ScheduleService;
import cn.archliu.horus.server.domain.schedule.task.CronTaskRegister;
import cn.archliu.horus.server.domain.schedule.task.HorusScheduleTask;
import cn.archliu.horus.server.domain.schedule.task.ScheduledTaskRunnable;
import cn.archliu.horus.server.domain.schedule.web.convert.ScheduleConvert;
import cn.archliu.horus.server.domain.schedule.web.dto.EditScheduleDTO;
import cn.archliu.horus.server.domain.schedule.web.dto.EditScheduleStateDTO;
import cn.archliu.horus.server.domain.schedule.web.dto.ScheduleJobDTO;
import cn.hutool.core.map.MapUtil;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private HorusScheduleJobMapper jobMapper;

    @Autowired
    private HorusGroovyInfoMapper groovyInfoMapper;

    @Autowired
    private CronTaskRegister taskRegister;

    @Autowired(required = false)
    private Map<String, HorusScheduleTask<?>> horusScheduleTasks;

    @Autowired
    private HorusScheduleHistoryMapper historyMapper;

    // TODO 后续可以改造使用 redis 锁进行并发控制，当前先用 synchronized
    @Override
    public synchronized void addCronTask(ScheduleJobDTO scheduleJobDTO) {
        HorusScheduleJob job = ScheduleConvert.INSTANCE.convert(scheduleJobDTO);
        // 校验 job_code 是否存在
        boolean exists = new LambdaQueryChainWrapper<>(jobMapper)
                .eq(HorusScheduleJob::getJobCode, scheduleJobDTO.getJobCode()).exists();
        if (exists) {
            throw ParamErrorException.throwE("该 jobCode: " + scheduleJobDTO.getJobCode() + " 的定时任务已存在！");
        }
        // 校验 execute_job_id 是否存在
        switch (scheduleJobDTO.getJobType()) {
            case BEAN:
                checkScheduleBean(scheduleJobDTO);
                break;
            case SCRIPT:
                checkScript(scheduleJobDTO);
                break;
            default:
                throw ParamErrorException.throwE("定时任务类型: " + scheduleJobDTO.getJobType() + "错误！");
        }
        // 落地
        jobMapper.insert(job);
        // 添加到定时任务中
        taskRegister.addCronTask(job.getJobCode(), job.getCornStr(), job.getJobType());
    }

    /**
     * 校验定时任务的 SpringBean
     * 
     * @param scheduleJobDTO
     */
    private void checkScheduleBean(ScheduleJobDTO scheduleJobDTO) {
        if (MapUtil.isEmpty(horusScheduleTasks) || !horusScheduleTasks.containsKey(scheduleJobDTO.getJobExecuteId())) {
            throw ParamErrorException.throwE("定时任务指定的执行 Bean 不存在！");
        }
    }

    /**
     * 校验定时任务的脚本
     * 
     * @param scheduleJobDTO
     */
    private void checkScript(ScheduleJobDTO scheduleJobDTO) {
        boolean exists = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                .eq(HorusGroovyInfo::getGroovyCode, scheduleJobDTO.getJobExecuteId()).exists();
        if (!exists) {
            throw ParamErrorException.throwE("定时任务指定的执行脚本不存在！");
        }
    }

    @Override
    public void deleteCronTask(String jobCode) {
        HorusScheduleJob job = new LambdaQueryChainWrapper<>(jobMapper).eq(HorusScheduleJob::getJobCode, jobCode).one();
        if (job == null) {
            throw ParamErrorException.throwE("该定时任务: " + jobCode + "不存在！");
        }
        // 内存中移除定时任务
        taskRegister.removeCronTask(jobCode);
        // DB 中删除定时任务
        QueryWrapper<HorusScheduleJob> deleteSql = new QueryWrapper<>();
        deleteSql.eq("job_code", jobCode);
        jobMapper.delete(deleteSql);
    }

    @Override
    public IPage<HorusScheduleJob> pageQuery(Page<HorusScheduleJob> page) {
        return jobMapper.selectPage(page, null);
    }

    @Override
    public IPage<HorusScheduleHistory> pageHistory(String jobCode, Page<HorusScheduleHistory> page) {
        return historyMapper.selectPage(page, new QueryWrapper<HorusScheduleHistory>().eq("job_code", jobCode));
    }

    // TODO 后续可以改造使用 redis 锁进行并发控制，当前先用 synchronized
    @Override
    public synchronized void editScheduleState(EditScheduleStateDTO editScheduleState) {
        HorusScheduleJob one = new LambdaQueryChainWrapper<>(jobMapper)
                .eq(HorusScheduleJob::getJobCode, editScheduleState.getJobCode()).one();
        if (one == null) {
            throw ParamErrorException.throwE("该定时任务 " + editScheduleState.getJobCode() + " 不存在！");
        }
        // 状态已经一致的不进行处理
        if (editScheduleState.getState().name().equalsIgnoreCase(one.getState())) {
            return;
        }
        // 启用定时任务
        if (ComState.ENABLED.equals(editScheduleState.getState())) {
            // 添加到定时任务中
            taskRegister.addCronTask(one.getJobCode(), one.getCornStr(), one.getJobType());
        } else {
            // 停用定时任务
            taskRegister.removeCronTask(one.getJobCode());
        }
        // 修改数据库的状态
        new LambdaUpdateChainWrapper<>(jobMapper).set(HorusScheduleJob::getState, editScheduleState.getState().name())
                .eq(HorusScheduleJob::getJobCode, one.getJobCode()).update();
    }

    @Override
    @Scheduled(cron = "${horus.server.schedule-history-clean-cron:0 0 * * *}")
    public void cleanHistory() {
        LambdaQueryWrapper<HorusScheduleHistory> sql = Wrappers.<HorusScheduleHistory>lambdaQuery()
                .lt(HorusScheduleHistory::getCreateTime, LocalDateTime.now().minusDays(7L));
        historyMapper.delete(sql);
    }

    @Override
    public void editCronTask(EditScheduleDTO editScheduleDTO) {
        // 1、先修改 db 中的
        new LambdaUpdateChainWrapper<>(jobMapper).set(HorusScheduleJob::getJobName, editScheduleDTO.getJobName())
                .set(HorusScheduleJob::getCornStr, editScheduleDTO.getCornStr())
                .set(HorusScheduleJob::getParamStr, editScheduleDTO.getParamStr())
                .eq(HorusScheduleJob::getJobCode, editScheduleDTO.getJobCode()).update();
        // 2、内存中移除定时任务
        taskRegister.removeCronTask(editScheduleDTO.getJobCode());
        // 3、添加到定时任务中
        HorusScheduleJob one = new LambdaQueryChainWrapper<>(jobMapper)
                .eq(HorusScheduleJob::getJobCode, editScheduleDTO.getJobCode()).one();
        taskRegister.addCronTask(one.getJobCode(), one.getCornStr(), one.getJobType());
    }

    @Override
    public void runSchedule(String jobCode) {
        // 创建任务
        ScheduledTaskRunnable taskRunnable = new ScheduledTaskRunnable(jobCode);
        // 执行任务
        taskRunnable.run();
    }

}
