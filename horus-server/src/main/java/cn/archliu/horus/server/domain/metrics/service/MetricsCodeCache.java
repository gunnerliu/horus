package cn.archliu.horus.server.domain.metrics.service;

import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import cn.archliu.horus.infr.domain.metrics.mapper.HorusMetricsDescMapper;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-05-24 22:25:31
 * @Description: metricsCode 缓存
 */
@Slf4j
@Component
public class MetricsCodeCache implements ApplicationRunner {

    /** metricsCode 缓存 */
    private Map<String, HorusMetricsDesc> metricsCodes;

    @Autowired
    private HorusMetricsDescMapper metricsDescMapper;

    /** 初始化缓存 */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init metricsCodes cache ...");
        metricsCodes = metricsDescMapper.selectList(null).stream()
                .collect(Collectors.toMap(HorusMetricsDesc::getMetricsCode, t -> t));
        log.info("init metricsCodes cache finished");
    }

    /**
     * 检查 metricsCode 是否存在
     * 
     * @param metricsCode
     * @return
     */
    public HorusMetricsDesc loadMetricsCode(String metricsCode) {
        if (StrUtil.isBlank(metricsCode)) {
            return null;
        }
        if (metricsCodes.containsKey(metricsCode)) {
            return metricsCodes.get(metricsCode);
        }

        HorusMetricsDesc one = new LambdaQueryChainWrapper<>(metricsDescMapper)
                .eq(HorusMetricsDesc::getMetricsCode, metricsCode).one();
        if (one != null) {
            metricsCodes.put(one.getMetricsCode(), one);
        }
        return metricsCodes.get(metricsCode);
    }

}
