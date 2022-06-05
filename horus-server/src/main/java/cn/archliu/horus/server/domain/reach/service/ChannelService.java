package cn.archliu.horus.server.domain.reach.service;

import java.util.List;

import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.entity.ReachChannel;

/**
 * @Author: Arch
 * @Date: 2022-05-28 14:48:07
 * @Description: 触达通道相关服务
 */
public interface ChannelService {

    /**
     * 匹配触达通道
     * 
     * @param message
     * @return
     */
    List<ReachChannel> pickChannel(HorusMessage message);

}
