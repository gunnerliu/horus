package cn.archliu.horus.client.metrics.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ApiMonitor {

    /** 打点时间 */
    private Timestamp markingTime = new Timestamp(System.currentTimeMillis());

    /** 链路ID */
    private String traceId;

    /** 链路节点ID */
    private String spanId;

    /** 请求地址 */
    private String requestUrl;

    /** 客户端 ip */
    private String clientIp;

    /** 请求类型 */
    private String method;

    /** 耗时 */
    private Long timeConsuming;

}
