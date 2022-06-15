package cn.archliu.horus.client.monitor.entity;

import java.sql.Timestamp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-03 22:48:48
 * @Description: 计数指标实体
 */
@Data
@Accessors(chain = true)
public class HorusCounter {

    private String traceId;

    private String spanId;

    /** 打点时间 */
    private Timestamp markingTime;

    /** 计数指标 code */
    private String counterCode;

    /** 标签 */
    private String label;

}
