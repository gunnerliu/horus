package cn.archliu.horus.server.domain.schedule.web.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cn.archliu.horus.infr.domain.schedule.entity.HorusScheduleJob;
import cn.archliu.horus.server.domain.schedule.web.dto.ScheduleJobDTO;

@Mapper
public interface ScheduleConvert {

    ScheduleConvert INSTANCE = Mappers.getMapper(ScheduleConvert.class);

    HorusScheduleJob convert(ScheduleJobDTO scheduleJobDTO);

    List<ScheduleJobDTO> convert(List<HorusScheduleJob> horusScheduleJobs);

}
