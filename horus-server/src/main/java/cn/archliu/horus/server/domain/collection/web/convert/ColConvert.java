package cn.archliu.horus.server.domain.collection.web.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cn.archliu.horus.infr.domain.collection.entity.HorusCounter;
import cn.archliu.horus.infr.domain.collection.entity.MetricsCol;
import cn.archliu.horus.server.domain.collection.web.dto.HorusCounterDTO;
import cn.archliu.horus.server.domain.collection.web.dto.MetricsColDTO;

@Mapper
public interface ColConvert {

    ColConvert INSTANCE = Mappers.getMapper(ColConvert.class);

    List<HorusCounter> convert(List<HorusCounterDTO> dtos);

    List<MetricsCol> toMetrics(List<MetricsColDTO> dtos);

}
