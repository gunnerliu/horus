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

    // TODO ???????????????????????? redis ???????????????????????????????????? synchronized
    @Override
    public synchronized void addCronTask(ScheduleJobDTO scheduleJobDTO) {
        HorusScheduleJob job = ScheduleConvert.INSTANCE.convert(scheduleJobDTO);
        // ?????? job_code ????????????
        boolean exists = new LambdaQueryChainWrapper<>(jobMapper)
                .eq(HorusScheduleJob::getJobCode, scheduleJobDTO.getJobCode()).exists();
        if (exists) {
            throw ParamErrorException.throwE("??? jobCode: " + scheduleJobDTO.getJobCode() + " ???????????????????????????");
        }
        // ?????? execute_job_id ????????????
        switch (scheduleJobDTO.getJobType()) {
            case BEAN:
                checkScheduleBean(scheduleJobDTO);
                break;
            case SCRIPT:
                checkScript(scheduleJobDTO);
                break;
            default:
                throw ParamErrorException.throwE("??????????????????: " + scheduleJobDTO.getJobType() + "?????????");
        }
        // ??????
        jobMapper.insert(job);
        // ????????????????????????
        taskRegister.addCronTask(job.getJobCode(), job.getCornStr(), job.getJobType());
    }

    /**
     * ????????????????????? SpringBean
     * 
     * @param scheduleJobDTO
     */
    private void checkScheduleBean(ScheduleJobDTO scheduleJobDTO) {
        if (MapUtil.isEmpty(horusScheduleTasks) || !horusScheduleTasks.containsKey(scheduleJobDTO.getJobExecuteId())) {
            throw ParamErrorException.throwE("??????????????????????????? Bean ????????????");
        }
    }

    /**
     * ???????????????????????????
     * 
     * @param scheduleJobDTO
     */
    private void checkScript(ScheduleJobDTO scheduleJobDTO) {
        boolean exists = new LambdaQueryChainWrapper<>(groovyInfoMapper)
                .eq(HorusGroovyInfo::getGroovyCode, scheduleJobDTO.getJobExecuteId()).exists();
        if (!exists) {
            throw ParamErrorException.throwE("?????????????????????????????????????????????");
        }
    }

    @Override
    public void deleteCronTask(String jobCode) {
        HorusScheduleJob job = new LambdaQueryChainWrapper<>(jobMapper).eq(HorusScheduleJob::getJobCode, jobCode).one();
        if (job == null) {
            throw ParamErrorException.throwE("???????????????: " + jobCode + "????????????");
        }
        // ???????????????????????????
        taskRegister.removeCronTask(jobCode);
        // DB ?????????????????????
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

    // TODO ???????????????????????? redis ???????????????????????????????????? synchronized
    @Override
    public synchronized void editScheduleState(EditScheduleStateDTO editScheduleState) {
        HorusScheduleJob one = new LambdaQueryChainWrapper<>(jobMapper)
                .eq(HorusScheduleJob::getJobCode, editScheduleState.getJobCode()).one();
        if (one == null) {
            throw ParamErrorException.throwE("??????????????? " + editScheduleState.getJobCode() + " ????????????");
        }
        // ????????????????????????????????????
        if (editScheduleState.getState().name().equalsIgnoreCase(one.getState())) {
            return;
        }
        // ??????????????????
        if (ComState.ENABLED.equals(editScheduleState.getState())) {
            // ????????????????????????
            taskRegister.addCronTask(one.getJobCode(), one.getCornStr(), one.getJobType());
        } else {
            // ??????????????????
            taskRegister.removeCronTask(one.getJobCode());
        }
        // ????????????????????????
        new LambdaUpdateChainWrapper<>(jobMapper).set(HorusScheduleJob::getState, editScheduleState.getState().name())
                .eq(HorusScheduleJob::getJobCode, one.getJobCode()).update();
    }

    @Override
    @Scheduled(cron = "${horus.server.schedule-history-clean-cron:0 0 0 * * ?}")
    public void cleanHistory() {
        LambdaQueryWrapper<HorusScheduleHistory> sql = Wrappers.<HorusScheduleHistory>lambdaQuery()
                .lt(HorusScheduleHistory::getCreateTime, LocalDateTime.now().minusDays(7L));
        historyMapper.delete(sql);
    }

    @Override
    public void editCronTask(EditScheduleDTO editScheduleDTO) {
        // 1???????????? db ??????
        new LambdaUpdateChainWrapper<>(jobMapper).set(HorusScheduleJob::getJobName, editScheduleDTO.getJobName())
                .set(HorusScheduleJob::getCornStr, editScheduleDTO.getCornStr())
                .set(HorusScheduleJob::getParamStr, editScheduleDTO.getParamStr())
                .eq(HorusScheduleJob::getJobCode, editScheduleDTO.getJobCode()).update();
        // 2??????????????????????????????
        taskRegister.removeCronTask(editScheduleDTO.getJobCode());
        // 3???????????????????????????
        HorusScheduleJob one = new LambdaQueryChainWrapper<>(jobMapper)
                .eq(HorusScheduleJob::getJobCode, editScheduleDTO.getJobCode()).one();
        taskRegister.addCronTask(one.getJobCode(), one.getCornStr(), one.getJobType());
    }

    @Override
    public void runSchedule(String jobCode) {
        // ????????????
        ScheduledTaskRunnable taskRunnable = new ScheduledTaskRunnable(jobCode);
        // ????????????
        taskRunnable.run();
    }

}
