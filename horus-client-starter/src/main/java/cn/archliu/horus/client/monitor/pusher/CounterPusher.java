package cn.archliu.horus.client.monitor.pusher;

import java.util.List;

import cn.archliu.horus.client.config.HorusClientProperties;
import cn.archliu.horus.client.monitor.entity.HorusCounter;
import cn.archliu.horus.client.monitor.feign.HorusFeign;

/**
 * @Author: Arch
 * @Date: 2022-05-03 12:16:55
 * @Description: 计数打点数据推送
 */
public class CounterPusher implements Runnable {

    private List<HorusCounter> counters;

    private HorusClientProperties clientProperties;

    private HorusFeign horusFeign;

    public CounterPusher(List<HorusCounter> counters, HorusClientProperties clientProperties,
            HorusFeign horusFeign) {
        this.counters = counters;
        this.clientProperties = clientProperties;
        this.horusFeign = horusFeign;
    }

    @Override
    public void run() {
        horusFeign.counterCol(counters, clientProperties.getAppName(), clientProperties.getAccessParty(),
                clientProperties.getInstanceId());
    }

}
