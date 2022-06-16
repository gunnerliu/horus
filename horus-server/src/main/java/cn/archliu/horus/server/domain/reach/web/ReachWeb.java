package cn.archliu.horus.server.domain.reach.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.CRUDData;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.infr.domain.reach.entity.HorusReachChannel;
import cn.archliu.horus.infr.domain.reach.entity.HorusReachReceiver;
import cn.archliu.horus.server.domain.reach.service.ChannelService;
import cn.archliu.horus.server.domain.reach.service.MessageReach;
import cn.archliu.horus.server.domain.reach.web.convert.ChannelConvert;
import cn.archliu.horus.server.domain.reach.web.convert.MessageConvert;
import cn.archliu.horus.server.domain.reach.web.dto.ChannelReceiverDTO;
import cn.archliu.horus.server.domain.reach.web.dto.HorusMessageDTO;
import cn.archliu.horus.server.domain.reach.web.dto.ReachChannelDTO;
import cn.archliu.horus.server.domain.reach.web.dto.ReachReceiverDTO;
import cn.archliu.horus.server.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@Api(tags = { "消息触达接口,内网(/inner/*)接口不做鉴权" })
@RestController
@RequestMapping({ "/inner/horus/reach", "/api/horus/reach" })
public class ReachWeb {

    @Autowired
    private MessageReach messageReach;

    @Autowired
    private ChannelService channelService;

    @ApiOperation("发送消息")
    @PutMapping("/sendMessage")
    public ComRes<ResData<Void>> sendMessage(@RequestBody HorusMessageDTO message) {
        messageReach.sendMessage(MessageConvert.INSTANCE.trans(message));
        return ComRes.success();
    }

    @ApiOperation("添加触达通道")
    @PutMapping("/addReachChannel")
    public ComRes<ResData<Void>> addReachChannel(@RequestBody ReachChannelDTO reachChannelDTO) {
        channelService.addReachChannel(ChannelConvert.INSTANCE.convert(reachChannelDTO));
        return ComRes.success();
    }

    @ApiOperation("添加消息接收人")
    @PutMapping("/addReceiver")
    public ComRes<ResData<Void>> addReceiver(@RequestBody ReachReceiverDTO reachReceiverDTO) {
        channelService.addReceiver(ChannelConvert.INSTANCE.convert(reachReceiverDTO));
        return ComRes.success();
    }

    @ApiOperation("添加触达通道的接收人")
    @PutMapping("/addChannelReceiver")
    public ComRes<ResData<Void>> addChannelReceiver(@RequestBody ChannelReceiverDTO channelReceiverDTO) {
        channelService.addChannelReceiver(channelReceiverDTO);
        return ComRes.success();
    }

    @ApiOperation("分页查询所有的触达通道")
    @GetMapping("/pageChannels")
    public ComRes<CRUDData<HorusReachChannel>> pageChannels(@RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize) {
        Page<HorusReachChannel> page = PageUtil.build(pageIndex, pageSize);
        Page<HorusReachChannel> data = channelService.pageChannels(page);
        return ComRes.success(new CRUDData<HorusReachChannel>().setItems(data.getRecords()).setTotal(data.getTotal()));
    }

    @ApiOperation("分页查询接收人")
    @GetMapping("/pageReceivers")
    public ComRes<CRUDData<HorusReachReceiver>> pageReceivers(
            @RequestParam(value = "channelId", required = false) Long channelId,
            @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize) {
        Page<HorusReachReceiver> page = PageUtil.build(pageIndex, pageSize);
        IPage<HorusReachReceiver> data = channelService.pageChannelReceivers(channelId, page);
        return ComRes.success(new CRUDData<HorusReachReceiver>().setItems(data.getRecords()).setTotal(data.getTotal()));
    }

    // TODO 编辑

}
