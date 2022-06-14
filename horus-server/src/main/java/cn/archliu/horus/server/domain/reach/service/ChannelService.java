package cn.archliu.horus.server.domain.reach.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.archliu.horus.infr.domain.reach.entity.HorusReachChannel;
import cn.archliu.horus.infr.domain.reach.entity.HorusReachReceiver;
import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.entity.ReachChannel;
import cn.archliu.horus.server.domain.reach.web.dto.ChannelReceiverDTO;

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

    /**
     * 添加触达通道
     * 
     * @param reachChannel
     */
    void addReachChannel(HorusReachChannel reachChannel);

    /**
     * reachReceiver
     * 
     * @param convert
     */
    void addReceiver(HorusReachReceiver reachReceiver);

    /**
     * 添加触达通道的接收人
     * 
     * @param channelReceiverDTO
     */
    void addChannelReceiver(ChannelReceiverDTO channelReceiverDTO);

    /**
     * 分页查询所有的触达通道
     * 
     * @param page
     * @return
     */
    Page<HorusReachChannel> pageChannels(Page<HorusReachChannel> page);

    /**
     * 查询通道下的接收人
     * 
     * @param channelId
     * @param page
     * @return
     */
    IPage<HorusReachReceiver> pageChannelReceivers(Long channelId, Page<HorusReachReceiver> page);

}
