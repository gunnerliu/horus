package cn.archliu.horus.infr.domain.reach.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
    List<HorusReachReceiver> loadByChannel(Long channelId);

}
