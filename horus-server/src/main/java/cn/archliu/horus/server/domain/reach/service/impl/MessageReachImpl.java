package cn.archliu.horus.server.domain.reach.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.archliu.horus.infr.domain.reach.entity.HorusReachMessage;
import cn.archliu.horus.infr.domain.reach.entity.HorusReachReceiver;
import cn.archliu.horus.infr.domain.reach.mapper.HorusReachMessageMapper;
import cn.archliu.horus.infr.domain.reach.mapper.HorusReachReceiverMapper;
import cn.archliu.horus.server.config.HorusServerProperties;
import cn.archliu.horus.server.config.HorusServerProperties.ReachInfo;
import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.entity.ReachChannel;
import cn.archliu.horus.server.domain.reach.entity.ReceiverInfo;
import cn.archliu.horus.server.domain.reach.enums.Level;
import cn.archliu.horus.server.domain.reach.enums.ReacherType;
import cn.archliu.horus.server.domain.reach.enums.SendState;
import cn.archliu.horus.server.domain.reach.reacher.HorusReacher;
import cn.archliu.horus.server.domain.reach.service.ChannelService;
import cn.archliu.horus.server.domain.reach.service.MessageReach;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageReachImpl implements MessageReach {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private HorusServerProperties horusServerProperties;

    @Autowired
    private HorusReachMessageMapper messageMapper;

    @Autowired
    private HorusReachReceiverMapper receiverMapper;

    @Async("msgReachExecutor")
    @Override
    public void sendMessage(HorusMessage message) {
        // ??????????????????
        List<ReachChannel> channels = channelService.pickChannel(message);
        switch (message.getLevel()) {
            case INSTANT: // ????????????
                processInstant(message, channels);
                break;
            case AGGREGATION: // ????????????
                processAggregation(message, channels);
                break;
            default:
                processInstant(message, channels);
        }
    }

    /**
     * ?????????????????????????????????????????????
     * 
     * @param message
     * @param channels
     */
    private void processInstant(HorusMessage message, List<ReachChannel> channels) {
        // ???????????????????????????????????????????????????????????????
        HorusReachMessage msg = new HorusReachMessage().setMsgCategoryCode(message.getCategoryCode())
                .setMsgTag(message.getTag()).setMsgContent(message.getContent())
                .setMsgLevel(message.getLevel().name());
        if (CollUtil.isEmpty(channels)) {
            msg.setSendState(SendState.FAIL.name()).setErrorMsg("?????????????????????????????????");
            messageMapper.insert(msg);
            return;
        }
        List<ReceiverInfo> collect = channels.stream()
                .filter(item -> item != null && CollUtil.isNotEmpty(item.getReceivers()))
                .flatMap(item -> item.getReceivers().stream()).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            msg.setSendState(SendState.FAIL.name()).setErrorMsg("?????????????????????????????????");
            messageMapper.insert(msg);
            return;
        }
        for (ReceiverInfo receiver : collect) {
            try {
                // -1 ??????????????????
                msg.setId(null).setReceiverId(receiver.getReceiverId() == null ? -1 : receiver.getReceiverId())
                        .setSendState(SendState.SUCCESS.name());
                // ????????????
                sendMessage(message, receiver);
            } catch (Exception e) {
                msg.setSendState(SendState.FAIL.name()).setErrorMsg(e.getMessage());
                log.error("????????????????????????!", e);
            }
            messageMapper.insert(msg);
        }
    }

    /**
     * ?????????????????????????????????????????????
     * 
     * @param message
     * @param channels
     */
    private void processAggregation(HorusMessage message, List<ReachChannel> channels) {
        // ???????????????????????????????????????????????????????????????
        HorusReachMessage msg = new HorusReachMessage().setMsgCategoryCode(message.getCategoryCode())
                .setMsgTag(message.getTag()).setMsgContent(message.getContent())
                .setMsgLevel(message.getLevel().name());
        if (CollUtil.isEmpty(channels)) {
            msg.setSendState(SendState.FAIL.name()).setErrorMsg("?????????????????????????????????");
            messageMapper.insert(msg);
            return;
        }
        List<ReceiverInfo> collect = channels.stream()
                .filter(item -> item != null && CollUtil.isNotEmpty(item.getReceivers()))
                .flatMap(item -> item.getReceivers().stream()).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            msg.setSendState(SendState.FAIL.name()).setErrorMsg("?????????????????????????????????");
            messageMapper.insert(msg);
            return;
        }
        for (ReceiverInfo receiver : collect) {
            // -1 ??????????????????
            msg.setId(null).setReceiverId(receiver.getReceiverId() == null ? -1 : receiver.getReceiverId())
                    .setSendState(SendState.PENDING.name());
            messageMapper.insert(msg);
        }
    }

    @Scheduled(fixedDelayString = "${horus.server.msg-aggregation-cycle:30000}")
    @Override
    public void aggregationMessagePush() {
        // ?????? PENDING ??????????????????
        List<HorusReachMessage> messages = new LambdaQueryChainWrapper<>(messageMapper)
                .eq(HorusReachMessage::getMsgLevel, Level.AGGREGATION.name())
                .eq(HorusReachMessage::getSendState, SendState.PENDING.name()).list();
        if (CollUtil.isEmpty(messages)) {
            log.info("????????????????????????");
            return;
        }
        Map<Long, List<HorusReachMessage>> groupMsg = new HashMap<>();
        for (HorusReachMessage msg : messages) {
            if (!groupMsg.containsKey(msg.getReceiverId())) {
                groupMsg.put(msg.getReceiverId(), ListUtil.toList(msg));
            } else {
                groupMsg.get(msg.getReceiverId()).add(msg);
            }
        }
        groupMsg.entrySet().stream().forEach(item -> {
            // ???????????????
            ReceiverInfo receiverInfo = loadReceiverInfo(item.getKey());
            if (receiverInfo == null) {
                log.warn("?????????ID??? {} ????????????", item.getKey());
                return;
            }
            // ??????????????????
            List<HorusReachMessage> msgs = item.getValue();
            if (CollUtil.isEmpty(msgs)) {
                log.warn("?????????????????????");
                return;
            }
            HorusMessage sendMessage = new HorusMessage();
            List<String> categoryCodes = msgs.stream().map(HorusReachMessage::getMsgCategoryCode)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(categoryCodes)) {
                sendMessage.setCategoryCode(String.join(", ", categoryCodes));
            }
            List<String> tags = msgs.stream().map(HorusReachMessage::getMsgTag)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(tags)) {
                sendMessage.setTag(String.join(", ", tags));
            }
            sendMessage.setContent("????????????????????? " + msgs.size() + " ??????????????????");
            SendState sendState = SendState.SUCCESS;
            String errMsg = null;
            // ????????????
            try {
                sendMessage(sendMessage, receiverInfo);
            } catch (Exception e) {
                sendState = SendState.FAIL;
                errMsg = e.getMessage();
                log.error("???????????????????????????", e);
            }
            // ????????????????????????
            new LambdaUpdateChainWrapper<>(messageMapper).set(HorusReachMessage::getSendState, sendState.name())
                    .set(HorusReachMessage::getErrorMsg, errMsg)
                    .in(HorusReachMessage::getId,
                            msgs.stream().map(HorusReachMessage::getId).collect(Collectors.toList()))
                    .update();
        });
    }

    /**
     * ???????????????
     * 
     * @param receiverId
     * @return
     */
    private ReceiverInfo loadReceiverInfo(Long receiverId) {
        ReceiverInfo receiverInfo = null;
        if (NumberUtil.equals(receiverId, -1L)) {
            ReachInfo reachInfo = horusServerProperties.getReachInfo();
            receiverInfo = new ReceiverInfo().setReceiverId(-1L).setReacherType(reachInfo.getReacherType())
                    .setWebHook(reachInfo.getWebHook()).setAtMobiles(reachInfo.getAtMobiles());
        } else {
            HorusReachReceiver receiver = receiverMapper.selectById(receiverId);
            receiverInfo = new ReceiverInfo().setReceiverId(receiver.getId())
                    .setReacherType(ReacherType.transThrowE(receiver.getReacher()))
                    .setWebHook(receiver.getWebHook()).setAtMobiles(ListUtil.toList(receiver.getMobile()));
        }
        return receiverInfo;
    }

    /**
     * ????????????
     * 
     * @param message
     * @param receiver
     */
    private void sendMessage(HorusMessage message, ReceiverInfo receiver) {
        HorusReacher reacher = HorusReacher.getReacher(receiver.getReacherType());
        reacher.sendMessage(message, receiver);
    }

}
