package cn.archliu.horus.server.domain.groovy.web.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cn.archliu.horus.infr.domain.groovy.entity.HorusGroovyInfo;
import cn.archliu.horus.server.domain.groovy.web.dto.AddGroovyDTO;

@Mapper
public interface GroovyConvert {

    GroovyConvert INSTANCE = Mappers.getMapper(GroovyConvert.class);

    HorusGroovyInfo trans(AddGroovyDTO addGroovyDTO);

}
