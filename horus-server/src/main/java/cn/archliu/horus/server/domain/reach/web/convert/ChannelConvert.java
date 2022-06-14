package cn.archliu.horus.server.domain.reach.web.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cn.archliu.horus.infr.domain.reach.entity.HorusReachChannel;
import cn.archliu.horus.infr.domain.reach.entity.HorusReachReceiver;
import cn.archliu.horus.server.domain.reach.web.dto.ReachChannelDTO;
import cn.archliu.horus.server.domain.reach.web.dto.ReachReceiverDTO;

@Mapper
public interface ChannelConvert {

    ChannelConvert INSTANCE = Mappers.getMapper(ChannelConvert.class);

    HorusReachChannel convert(ReachChannelDTO dto);

    HorusReachReceiver convert(ReachReceiverDTO dto);

}
