package cn.archliu.horus.client.monitor.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-03 22:52:57
 * @Description: 指标打点
 */
@Data
@Accessors(chain = true)
public class HorusMetrics {

    /** 指标 code */
    private String metricsCode;

    /** 指标字段 */
    private List<String> metricsColumns;

    /** 指标字段值 */
    private List<List<Object>> metricsColumnsValue;

    /** 分隔标签 */
    private String splitTag = "basic";

}
