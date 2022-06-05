package cn.archliu.horus.server.domain.collection.filter.sub;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.archliu.horus.client.metrics.enums.MetricsCodes;
import cn.archliu.horus.common.enums.OnlineState;
import cn.archliu.horus.infr.domain.collection.entity.HorusApplicationInfo;
import cn.archliu.horus.infr.domain.collection.entity.MetricsCol;
import cn.archliu.horus.infr.domain.collection.mapper.HorusApplicationInfoMapper;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import cn.archliu.horus.server.domain.collection.filter.HorusMetricsFilter;
import cn.archliu.horus.server.domain.reach.entity.HorusMessage;
import cn.archliu.horus.server.domain.reach.enums.Level;
import cn.archliu.horus.server.domain.reach.service.MessageReach;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppAliveFilter implements HorusMetricsFilter {

    @Autowired
    private HorusApplicationInfoMapper applicationInfoMapper;

    @Autowired
    private MessageReach messageReach;

    @Override
    public void process(HorusMetricsDesc metricsDesc, MetricsCol metric, String appName, String accessParty) {
        if (metricsDesc == null || metric == null || CollUtil.isEmpty(metric.getMetricsColumns())
                || CollUtil.isEmpty(metric.getMetricsColumnsValue())) {
            return;
        }
        Map<String, HorusApplicationInfo> appInfos = new HashMap<>();
        // applicationName、appIp、appPort
        int[] columnIndex = new int[3];
        // 找到所需要的列
        List<String> metricsColumns = metric.getMetricsColumns();
        for (int i = 0; i < metricsColumns.size(); i++) {
            if (StrUtil.equals(metricsColumns.get(i), "application_name")) {
                columnIndex[0] = i;
            }
            if (StrUtil.equals(metricsColumns.get(i), "app_ip")) {
                columnIndex[1] = i;
            }
            if (StrUtil.equals(metricsColumns.get(i), "app_port")) {
                columnIndex[2] = i;
            }
        }
        List<List<Object>> metricsColumnsValue = metric.getMetricsColumnsValue();
        for (List<Object> columnValues : metricsColumnsValue) {
            String key = new StringBuilder().append(columnValues.get(columnIndex[0])).append("_")
                    .append(columnValues.get(columnIndex[1])).append("_").append(columnValues.get(columnIndex[2]))
                    .toString();
            if (!appInfos.containsKey(key)) {
                HorusApplicationInfo appInfo = new HorusApplicationInfo().setAppName(appName)
                        .setAccessParty(accessParty).setLastHeatTime(LocalDateTime.now())
                        .setApplicationName(StrUtil.toString(columnValues.get(columnIndex[0])))
                        .setAppIp(StrUtil.toString(columnValues.get(columnIndex[1])))
                        .setAppPort(Integer.valueOf(StrUtil.toString(columnValues.get(columnIndex[2]))))
                        .setOnlineState(OnlineState.ONLINE.name());
                appInfos.put(key, appInfo);
            }
        }
        // app 信息落地
        processAppInfos(appInfos);

    }

    /**
     * app 信息落地
     * 
     * @param appInfos
     */
    private void processAppInfos(Map<String, HorusApplicationInfo> appInfos) {
        if (MapUtil.isNotEmpty(appInfos)) {
            appInfos.entrySet().stream().map(Entry::getValue).forEach(item -> {
                HorusApplicationInfo one = new LambdaQueryChainWrapper<>(applicationInfoMapper)
                        .eq(HorusApplicationInfo::getApplicationName, item.getApplicationName())
                        .eq(HorusApplicationInfo::getAppIp, item.getAppIp())
                        .eq(HorusApplicationInfo::getAppPort, item.getAppPort()).one();
                if (one == null) {
                    applicationInfoMapper.insert(item);
                    // 新服务上线通知
                    messageReach.sendMessage(new HorusMessage().setCategoryCode("appCheck").setTag("appOnline")
                            .setLevel(Level.INSTANT).setContent("新服务上线： " + item.getApplicationName() + ", "
                                    + item.getAppIp() + ":" + item.getAppPort()));
                    log.info("新服务上线: {}", item.toString());
                } else {
                    String state = one.getOnlineState();
                    new LambdaUpdateChainWrapper<>(applicationInfoMapper)
                            .set(HorusApplicationInfo::getLastHeatTime, item.getLastHeatTime())
                            .set(HorusApplicationInfo::getOnlineState, OnlineState.ONLINE.name())
                            .eq(HorusApplicationInfo::getId, one.getId()).update();
                    // 如果由离线变更为在线可以发送通知
                    if (!OnlineState.ONLINE.name().equals(state)) {
                        messageReach.sendMessage(new HorusMessage().setCategoryCode("appCheck").setTag("appOnline")
                                .setLevel(Level.INSTANT).setContent("服务上线： " + item.getApplicationName() + " , "
                                        + item.getAppIp() + " , " + item.getAppPort()));
                        log.info("服务上线: {}", item.toString());
                    }
                }
            });
        }
    }

    @Override
    public String filterMetricsCode() {
        return MetricsCodes.APP_MONITOR;
    }

}
