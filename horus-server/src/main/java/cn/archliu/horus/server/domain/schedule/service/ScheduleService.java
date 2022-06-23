package cn.archliu.horus.server.domain.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleHistory;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.server.domain.schedule.web.dto.EditScheduleDTO;
import cn.archliu.horus.server.domain.schedule.web.dto.EditScheduleStateDTO;
import cn.archliu.horus.server.domain.schedule.web.dto.ScheduleJobDTO;

/**
 * @Author: Arch
 * @Date: 2022-04-24 22:58:36
 * @Description: 定时任务相关服务
 */
public interface ScheduleService {

    /**
     * 添加定时任务
     * 
     * @param scheduleJobDTO
     */
    void addCronTask(ScheduleJobDTO scheduleJobDTO);

    /**
     * 删除定时任务
     * 
     * @param jobCode
     */
    void deleteCronTask(String jobCode);

    /**
     * 分页查询
     * 
     * @param pageIndex
     * @param pageSize
     * @return
     */
    IPage<HorusScheduleJob> pageQuery(Page<HorusScheduleJob> page);

    /**
     * 分页查询执行历史
     * 
     * @param jobCode
     * @param page
     * @return
     */
    IPage<HorusScheduleHistory> pageHistory(String jobCode, Page<HorusScheduleHistory> page);

    /**
     * 修改定时任务状态
     * 
     * @param editScheduleState
     */
    void editScheduleState(EditScheduleStateDTO editScheduleState);

    /**
     * 每天 0 点清除 7 天之前的定时任务执行历史
     */
    void cleanHistory();

    /**
     * 修改定时任务
     * 
     * @param editScheduleDTO
     */
    void editCronTask(EditScheduleDTO editScheduleDTO);

    /**
     * 执行任务
     * 
     * @param jobCode
     */
    void runSchedule(String jobCode);

}
