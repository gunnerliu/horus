package cn.archliu.horus.infr.domain.reach.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import cn.archliu.horus.infr.domain.reach.entity.HorusReachChannel;

/**
 * <p>
 * 消息触达通道 Mapper 接口
 * </p>
 *
 * @author Arch
 * @since 2022-05-28
 */
public interface HorusReachChannelMapper extends BaseMapper<HorusReachChannel> {

    /**
     * 触达通道添加接收人
     * 
     * @param channelId
     * @param receiverId
     */
    void addChannelReceiver(@Param("channelId") Long channelId, @Param("receiverId") Long receiverId);

    /**
     * 检查该触达通道是否存在该接收人
     * 
     * @param channelId
     * @param receiverId
     * @return
     */
    boolean channelReceiverExists(@Param("channelId") Long channelId, @Param("receiverId") Long receiverId);

}
