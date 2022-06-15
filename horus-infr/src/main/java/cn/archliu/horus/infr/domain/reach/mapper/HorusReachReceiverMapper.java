package cn.archliu.horus.infr.domain.reach.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.Param;

import cn.archliu.horus.infr.domain.reach.entity.HorusReachReceiver;

/**
 * <p>
 * 消息接收人 Mapper 接口
 * </p>
 *
 * @author Arch
 * @since 2022-05-28
 */
public interface HorusReachReceiverMapper extends BaseMapper<HorusReachReceiver> {

    /**
     * 根据触达通道 ID 查询所有通知人
     * 
     * @param channelId
     * @return
     */
    List<HorusReachReceiver> loadByChannel(@Param("channelId") Long channelId);

    /**
     * 分页查询触达通道的接收人
     * 
     * @param page
     * @param channelId
     * @return
     */
    IPage<HorusReachReceiver> pageChannelReceivers(IPage<HorusReachReceiver> page, @Param("channelId") Long channelId);

    /**
     * 分页查询接收人
     * 
     * @param page
     * @param channelId
     * @return
     */
    IPage<HorusReachReceiver> pageReceivers(IPage<HorusReachReceiver> page);

}
