package cn.archliu.horus.server.domain.schedule.web;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.CRUDData;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.server.domain.schedule.service.ScheduleService;
import cn.archliu.horus.server.domain.schedule.web.convert.ScheduleConvert;
import cn.archliu.horus.server.domain.schedule.web.dto.ScheduleJobDTO;
import cn.archliu.horus.server.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Arch
 * @Date: 2022-04-24 15:21:17
 * @Description: 定时任务接口
 */
@Validated
@Api(tags = { "定时任务相关接口" })
@RequestMapping("/api/horus/schedule")
@RestController
public class ScheduleWeb {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("分页查询定时任务")
    @PostMapping("/pageQuery")
    public ComRes<CRUDData<ScheduleJobDTO>> pageQuery(@RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize) {
        Page<HorusScheduleJob> page = PageUtil.build(pageIndex, pageSize);
        IPage<HorusScheduleJob> data = scheduleService.pageQuery(page);
        return ComRes.success(PageUtil.build(data, ScheduleConvert.INSTANCE::convert));
    }

    @ApiOperation("添加定时任务")
    @PutMapping("/addCronTask")
    public ComRes<ResData<Void>> addCronTask(@Validated @RequestBody ScheduleJobDTO scheduleJobDTO) {
        scheduleService.addCronTask(scheduleJobDTO);
        return ComRes.success();
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping("/deleteCronTask")
    public ComRes<ResData<Void>> deleteCronTask(
            @Valid @NotBlank(message = "jobCode不允许为空") @RequestParam("jobCode") String jobCode) {
        scheduleService.deleteCronTask(jobCode);
        return ComRes.success();
    }

    @ApiOperation("修改定时任务")
    @PostMapping("/editCronTask")
    public ComRes<ResData<Void>> editCronTask() {
        // TODO 修改定时任务
        return ComRes.success();
    }

}
