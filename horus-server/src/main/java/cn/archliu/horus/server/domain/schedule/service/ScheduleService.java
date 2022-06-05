package cn.archliu.horus.server.domain.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.server.domain.schedule.web.dto.ScheduleJobDTO;

/**
 * @Author: Arch
 * @Date: 2022-04-24 15:58:36
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

}
