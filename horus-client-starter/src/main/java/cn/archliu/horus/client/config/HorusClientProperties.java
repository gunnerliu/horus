package cn.archliu.horus.client.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "horus.client")
public class HorusClientProperties implements ApplicationListener<WebServerInitializedEvent> {

    /** 是否监控接口打点、耗时等相关数据 */
    private Boolean monitorApi = Boolean.TRUE;

    /** 是否打印监控接口日志 */
    private boolean monitorApiLog = Boolean.FALSE;

    /** 是否监控 logback 的 error 日志 */
    private Boolean monitorLogback = Boolean.FALSE;

    /** 是否监控 actuator 应用数据 */
    private Boolean monitorApp = Boolean.FALSE;

    /** horus 服务的地址 */
    private String horusHost;

    /** 推送打点数据的周期,单位秒,默认 15 秒 */
    private String pushCycle = "*/15 * * * * ?";

    /** 服务名,只允许下划线与字母 */
    @Value("${spring.application.name:UNDEFINED}")
    private String appName;

    /** 接入方,只允许下划线与字母 */
    private String accessParty = "UNDEFINED";

    /** 实例名,IP:PORT,没有值则默认取IPV4地址:Port */
    private String instanceId;

    /** 用于端口获取 */
    private Integer port;

    /** 应用的 IPV4 地址 */
    private String ip;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        port = event.getWebServer().getPort();
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            instanceId = ip + ":" + port;
        } catch (UnknownHostException e) {
            log.error("获取IP失败", e);
            ip = "UNDEFINED";
            instanceId = "UNDEFINED";
        }
    }

}
