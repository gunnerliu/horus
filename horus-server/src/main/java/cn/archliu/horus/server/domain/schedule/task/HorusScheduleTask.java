package cn.archliu.horus.server.domain.schedule.task;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

import cn.archliu.horus.common.enums.ComState;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.infr.domain.schedule.mapper.HorusScheduleJobMapper;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-04-24 22:29:19
 * @Description: 定时任务抽象类
 */
@SuppressWarnings({ "squid:S1610" })
@Slf4j
public abstract class HorusScheduleTask<T> {

    @SuppressWarnings({ "unchecked" })
    public void run(String jobCode) {
        if (CharSequenceUtil.isBlank(jobCode)) {
            log.warn("定时任务执行失败, jobCode 为空！");
            return;
        }
        HorusScheduleJobMapper scheduleMapper = SpringUtil.getBean(HorusScheduleJobMapper.class);
        HorusScheduleJob job = new LambdaQueryChainWrapper<>(scheduleMapper)
                .eq(HorusScheduleJob::getJobCode, jobCode)
                .eq(HorusScheduleJob::getState, ComState.ENABLED.name()).one();
        if (job == null) {
            log.warn("定时任务 jobCode: {} 已删除或已停用！", jobCode);
            return;
        }
        // 定时任务参数
        String paramStr = job.getParamStr();
        T param = null;
        if (CharSequenceUtil.isNotBlank(paramStr)) {
            Type type = this.getClass().getGenericSuperclass();
            Class<T> paramClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
            if (String.class.equals(paramClass)) {
                param = (T) paramStr;
            } else {
                param = JSONUtil.toBean(paramStr, paramClass);
            }
        }
        log.info("定时任务开始执行-jobCode: {}, 参数: {}", jobCode, param == null ? "空" : param.toString());
        try {
            this.process(param);
        } catch (Exception e) {
            log.error("定时任务执行异常！", e);
            throw e;
        }
    }

    /**
     * 子类需要实现的方法,业务逻辑
     * 
     * @param param 数据库中 param_str json字符串反序列化
     */
    public abstract void process(T param);

    /**
     * job_execute_id
     * 
     * @return job_execute_id
     */
    public abstract String jobExecuteId();

}
