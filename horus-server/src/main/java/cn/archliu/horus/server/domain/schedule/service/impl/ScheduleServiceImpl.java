package cn.archliu.horus.server.domain.schedule.service.impl;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.archliu.horus.common.exception.sub.ParamErrorException;
import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.infr.domain.groovy.mapper.HorusGroovyInfoMapper;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.infr.domain.schedule.mapper.HorusScheduleJobMapper;
import cn.archliu.horus.server.domain.schedule.service.ScheduleService;
import cn.archliu.horus.server.domain.schedule.task.CronTaskRegister;
import cn.archliu.horus.server.domain.schedule.task.HorusScheduleTask;
import cn.archliu.horus.server.domain.schedule.web.convert.ScheduleConvert;
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

    @Override
    public void addCronTask(ScheduleJobDTO scheduleJobDTO) {
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

}
