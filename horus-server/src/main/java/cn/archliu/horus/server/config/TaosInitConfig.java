package cn.archliu.horus.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import cn.archliu.horus.infr.domain.metrics.mapper.HorusTaosInitMapper;
import cn.archliu.horus.server.domain.metrics.service.MetricsDescService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-04-27 22:55:40
 * @Description: TDEngine 初始化配置
 */
@Slf4j
@Configuration
public class TaosInitConfig implements CommandLineRunner {

    @Autowired
    private HorusTaosInitMapper taosInitMapper;

    @Autowired
    private MetricsDescService metricsDescService;

    /**
     * 容器启动初始化参数
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("init taos config start ...");
        this.initTaos();
        log.info("init taos config finished");
    }

    /**
     * 初始化 TDEngine：
     * 1、初始化建库
     * 2、初始化建表
     */
    private void initTaos() {
        // 初始化 Metrics
        initMetrics();
    }

    /**
     * 初始化 Metrics
     */
    private void initMetrics() {
        // 1、初始化 metrics 数据库
        taosInitMapper.initMetricsDB();
        // 2、创建计数指标超级表
        taosInitMapper.initCounterST();
        // 3、捞出没有在 TDEngine 中建表的 metricsCode 进行建表
        metricsDescService.createTaosSTables();
    }

}
