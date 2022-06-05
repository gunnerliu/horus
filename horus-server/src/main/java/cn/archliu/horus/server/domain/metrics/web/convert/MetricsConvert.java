package cn.archliu.horus.server.domain.metrics.web.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsColumns;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsColumnDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.MetricsColumnDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.MetricsDTO;

@Mapper
public interface MetricsConvert {

    MetricsConvert INSTANCE = Mappers.getMapper(MetricsConvert.class);

    HorusMetricsDesc convert(AddMetricsDTO addMetricsDTO);

    HorusMetricsColumns convert(AddMetricsColumnDTO addMetricsColumn);

    List<MetricsDTO> convert(List<HorusMetricsDesc> horusMetricsDescs);

    List<MetricsColumnDTO> toColumnDTOs(List<HorusMetricsColumns> horusMetricsColumns);

}
