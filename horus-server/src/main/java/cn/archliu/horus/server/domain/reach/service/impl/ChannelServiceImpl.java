package cn.archliu.horus.server.domain.reach.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import cn.archliu.common.exception.sub.ParamErrorException;
import cn.archliu.horus.infr.domain.reach.entity.HorusReachChannel;
import cn.archliu.horus.infr.domain.reach.entity.HorusReachReceiver;
import cn.archliu.horus.infr.domain.reach.mapper.HorusReachChannelMapper;
import cn.archliu.horus.infr.domain.reach.mapper.HorusReachReceiverMapper;
import cn.archliu.horus.server.config.HorusServerProperties;
import cn.archliu.horus.server.config.HorusServerProperties.ReachInfo;
import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.entity.ReachChannel;
import cn.archliu.horus.server.domain.reach.entity.ReceiverInfo;
import cn.archliu.horus.server.domain.reach.enums.ReacherType;
import cn.archliu.horus.server.domain.reach.service.ChannelService;
import cn.archliu.horus.server.domain.reach.web.dto.ChannelReceiverDTO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class ChannelServiceImpl implements ChannelService, ApplicationRunner {

    private ReachChannel defaultChannel;

    @Autowired
    private HorusServerProperties horusServerProperties;

    @Autowired
    private HorusReachChannelMapper channelMapper;

    @Autowired
    private HorusReachReceiverMapper receiverMapper;

    @Override
    public List<ReachChannel> pickChannel(HorusMessage message) {
        if (StrUtil.isBlank(message.getCategoryCode())) {
            // 返回默认的通道
            return ListUtil.toList(defaultChannel);
        }
        // 根据 categoryCode 匹配触达通道
        List<HorusReachChannel> channels = new LambdaQueryChainWrapper<>(channelMapper)
                .eq(HorusReachChannel::getCategoryCode, message.getCategoryCode()).list();
        if (CollUtil.isEmpty(channels)) {
            return ListUtil.toList(defaultChannel);
        }
        List<ReachChannel> channelList = new ArrayList<>();
        for (HorusReachChannel channel : channels) {
            // 捞出通道的所有通知人
            List<HorusReachReceiver> loadByChannel = receiverMapper.loadByChannel(channel.getId());
            if (CollUtil.isEmpty(loadByChannel)) {
                continue;
            }
            List<ReceiverInfo> collect = loadByChannel.stream()
                    .map(item -> new ReceiverInfo().setReceiverId(item.getId()).setWebHook(item.getWebHook())
                            .setReacherType(ReacherType.transThrowE(item.getReacher()))
                            .setAtMobiles(ListUtil.toList(item.getMobile())))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(collect)) {
                continue;
            }
            channelList.add(new ReachChannel().setChannelCode(channel.getChannelCode()).setReceivers(collect));
        }
        return channelList;
    }

    @Override
    public void addReachChannel(HorusReachChannel reachChannel) {
        boolean exists = new LambdaQueryChainWrapper<>(channelMapper)
                .eq(HorusReachChannel::getChannelCode, reachChannel.getChannelCode()).exists();
        if (exists) {
            throw ParamErrorException.throwE("该触达通道已经存在！");
        }
        // 数据落地
        channelMapper.insert(reachChannel);
    }

    @Override
    public void addReceiver(HorusReachReceiver reachReceiver) {
        receiverMapper.insert(reachReceiver);
    }

    @Override
    public void addChannelReceiver(ChannelReceiverDTO channelReceiverDTO) {
        boolean channelExists = new LambdaQueryChainWrapper<>(channelMapper)
                .eq(HorusReachChannel::getId, channelReceiverDTO.getChannelId()).exists();
        boolean receiverExists = new LambdaQueryChainWrapper<>(receiverMapper)
                .eq(HorusReachReceiver::getId, channelReceiverDTO.getReceiverId()).exists();
        if (!channelExists || !receiverExists) {
            throw ParamErrorException.throwE("触达通道或者接收人不存在！");
        }
        if (channelMapper.channelReceiverExists(channelReceiverDTO.getChannelId(),
                channelReceiverDTO.getReceiverId())) {
            throw ParamErrorException.throwE("该触达通道已经存在该接收人！");
        }
        // 数据落地
        channelMapper.addChannelReceiver(channelReceiverDTO.getChannelId(), channelReceiverDTO.getReceiverId());
    }

    @Override
    public Page<HorusReachChannel> pageChannels(Page<HorusReachChannel> page) {
        return channelMapper.selectPage(page, null);
    }

    @Override
    public IPage<HorusReachReceiver> pageChannelReceivers(Long channelId, Page<HorusReachReceiver> page) {
        return channelId == null ? receiverMapper.pageReceivers(page)
                : receiverMapper.pageChannelReceivers(page, channelId);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 组织默认的触达通道
        ReachInfo reachInfo = horusServerProperties.getReachInfo();
        defaultChannel = new ReachChannel().setChannelCode("default")
                .setReceivers(ListUtil.toList(new ReceiverInfo().setWebHook(reachInfo.getWebHook())
                        .setReacherType(reachInfo.getReacherType()).setAtMobiles(reachInfo.getAtMobiles())));
    }

}
