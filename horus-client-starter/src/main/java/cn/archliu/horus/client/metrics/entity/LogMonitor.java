package cn.archliu.horus.client.metrics.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class LogMonitor {

    /** 打点时间 */
    private Timestamp markingTime = new Timestamp(System.currentTimeMillis());

    /** 链路ID */
    private String traceId;

    /** 链路节点ID */
    private String spanId;

    /** 日志类 */
    private String logger;

    /** 日志信息 */
    private String message;

    /** 异常名 */
    private String throwName;

    /** 异常信息 */
    private String throwMsg;

}
