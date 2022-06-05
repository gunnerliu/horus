package cn.archliu.horus.server.domain.reach.web.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.web.dto.HorusMessageDTO;

@Mapper
public interface MessageConvert {

    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    HorusMessage trans(HorusMessageDTO messageDTO);

}
