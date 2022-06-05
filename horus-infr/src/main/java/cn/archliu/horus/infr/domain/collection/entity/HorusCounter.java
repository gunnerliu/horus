package cn.archliu.horus.infr.domain.collection.entity;

import java.sql.Timestamp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HorusCounter {

    /** 主键 */
    private Timestamp ts;

    /** 打点时间 */
    private Timestamp markingTime;

    /** 计数指标 code */
    private String counterCode;

    /** 服务名 */
    private String appName;

    /** 接入方 */
    private String accessParty;

    private String traceId;

    private String spanId;

    /** 标签 */
    private String label;

}
