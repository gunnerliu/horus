package cn.archliu.horus.client.monitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.archliu.horus.client.config.HorusClientProperties;
import cn.archliu.horus.client.monitor.entity.HorusCounter;
import cn.archliu.horus.client.monitor.feign.HorusFeign;
import cn.archliu.horus.client.monitor.pusher.CounterPusher;
import cn.archliu.horus.client.monitor.pusher.MetricsPusher;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-05-03 10:47:57
 * @Description: horus 指标监控服务
 */
@Slf4j
@Component
public class HorusEye {

    /** 计数打点缓存,写多读少,用 LinkedList */
    private List<HorusCounter> counters = new LinkedList<>();

    /** 指标打点缓存,写多读少,用 LinkedList */
    private Map<String, List<Object>> metrics = new ConcurrentHashMap<>();

    /** 计数打点的锁 */
    private final ReentrantLock counterLock = new ReentrantLock();

    /** 指标打点的锁 */
    private final ReentrantLock metricsLock = new ReentrantLock();

    @Autowired
    private HorusClientProperties clientProperties;

    @Autowired
    private HorusFeign horusEyeFeign;

    /**
     * 计数打点
     * 
     * @param counter
     */
    public void counter(HorusCounter counter) {
        counterLock.lock();
        try {
            counters.add(counter);
        } catch (Exception e) {
            log.error("计数打点异常！", e);
        } finally {
            counterLock.unlock();
        }
    }

    /**
     * 指标打点
     * 
     * @param metricsCode
     * @param metric
     */
    public void mark(String metricsCode, Object metric) {
        metricsLock.lock();
        try {
            if (metrics.containsKey(metricsCode)) {
                metrics.get(metricsCode).add(metric);
            } else {
                metrics.put(metricsCode, ListUtil.toList(metric));
            }
        } catch (Exception e) {
            log.error("指标打点异常！", e);
        } finally {
            metricsLock.unlock();
        }
    }

    /**
     * 新起线程，将打点数据缓存移动到新的线程中，新的线程将打点数据推送到 horus
     */
    @Scheduled(cron = "${horus.client.push-cycle:*/15 * * * * ?}")
    protected void pushCache() {
        // 创建推送计数打点数据
        createCounterPusher();
        // 创建推送指标打点数据
        createMetricsPusher();
    }

    /**
     * 创建推送计数打点数据
     */
    private void createCounterPusher() {
        counterLock.lock();
        try {
            if (CollUtil.isNotEmpty(this.counters)) {
                List<HorusCounter> countersMV = this.counters;
                this.counters = new LinkedList<>();
                Thread thread = new Thread(new CounterPusher(countersMV, clientProperties, horusEyeFeign));
                thread.start();
            }
        } catch (Exception e) {
            log.error("创建计数打点推送线程失败！", e);
        } finally {
            counterLock.unlock();
        }
    }

    /**
     * 创建推送指标打点数据
     */
    private void createMetricsPusher() {
        metricsLock.lock();
        try {
            if (MapUtil.isNotEmpty(this.metrics)) {
                Map<String, List<Object>> metricsMV = this.metrics;
                this.metrics = new ConcurrentHashMap<>();
                Thread thread = new Thread(new MetricsPusher(metricsMV, clientProperties, horusEyeFeign));
                thread.start();
            }
        } catch (Exception e) {
            log.error("创建指标打点推送线程失败！", e);
        } finally {
            metricsLock.unlock();
        }
    }

}
