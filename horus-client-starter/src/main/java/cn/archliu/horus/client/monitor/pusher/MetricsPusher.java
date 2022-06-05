package cn.archliu.horus.client.monitor.pusher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.archliu.horus.client.config.HorusClientProperties;
import cn.archliu.horus.client.monitor.entity.HorusMetrics;
import cn.archliu.horus.client.monitor.feign.HorusFeign;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @Author: Arch
 * @Date: 2022-05-03 12:14:23
 * @Description: 指标打点数据推送
 */
public class MetricsPusher implements Runnable {

    private Map<String, List<Object>> metrics;

    private HorusClientProperties clientProperties;

    private HorusFeign horusFeign;

    public MetricsPusher(Map<String, List<Object>> metrics, HorusClientProperties clientProperties,
            HorusFeign horusFeign) {
        this.metrics = metrics;
        this.clientProperties = clientProperties;
        this.horusFeign = horusFeign;
    }

    @Override
    public void run() {
        List<HorusMetrics> metricsCols = new ArrayList<>();
        Iterator<Entry<String, List<Object>>> iterator = metrics.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, List<Object>> next = iterator.next();
            HorusMetrics horusMetrics = new HorusMetrics();
            horusMetrics.setMetricsCode(next.getKey());
            if (CollUtil.isEmpty(next.getValue())) {
                continue;
            }
            // 组织字段列表
            Object object = next.getValue().get(0);
            Field[] declaredFields = object.getClass().getDeclaredFields();
            List<String> columnNames = new ArrayList<>();
            for (Field field : declaredFields) {
                // 属性名
                String fieldName = field.getName();
                columnNames.add(fieldName);
            }
            // 设置字段列表
            horusMetrics.setMetricsColumns(columnNames);
            List<List<Object>> allColumnValues = new ArrayList<>();
            // 组织字段值列表
            for (Object obj : next.getValue()) {
                JSONObject parseObj = JSONUtil.parseObj(obj);
                List<Object> columnValues = new ArrayList<>();
                for (String columnName : columnNames) {
                    columnValues.add(parseObj.get(columnName));
                }
                allColumnValues.add(columnValues);
            }
            // 设置字段值列表
            horusMetrics.setMetricsColumnsValue(allColumnValues);
            metricsCols.add(horusMetrics);
        }
        // push
        horusFeign.metricsCol(metricsCols, clientProperties.getAppName(), clientProperties.getAccessParty(),
                clientProperties.getInstanceId());
    }

}
