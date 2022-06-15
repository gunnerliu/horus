package cn.archliu.horus.server.domain.metrics.enums;

import lombok.Getter;

/**
 * @Author: Arch
 * @Date: 2022-04-27 22:27:20
 * @Description: 指标类型
 */
@Getter
public enum MetricsType {

    COUNTER, // 计数
    METRICS; // 指标
}
