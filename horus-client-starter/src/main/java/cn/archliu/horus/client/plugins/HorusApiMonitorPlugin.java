package cn.archliu.horus.client.plugins;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yomahub.tlog.constant.TLogConstants;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.archliu.horus.client.metrics.entity.ApiMonitor;
import cn.archliu.horus.client.metrics.enums.MetricsCodes;
import cn.archliu.horus.client.monitor.HorusEye;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Arch
 * @Date: 2022-05-03 09:47:28
 * @Description: api 指标监控
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("horusApiMonitorPlugin")
@ConditionalOnProperty(prefix = "horus.client", name = "monitor-api", havingValue = "true", matchIfMissing = true)
public class HorusApiMonitorPlugin implements Filter {

    private Boolean monitorApiLog;

    private HorusEye horusEye;

    protected HorusApiMonitorPlugin() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        // OPTIONS 请求不进行打点
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            return;
        }
        ApiMonitor apiMonitor = new ApiMonitor().setMarkingTime(new Timestamp(System.currentTimeMillis()));
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String traceId = httpServletResponse.getHeader(TLogConstants.TLOG_TRACE_KEY);
        String spanId = httpServletResponse.getHeader(TLogConstants.TLOG_SPANID_KEY);
        String requestUri = StrUtil.replace(httpServletRequest.getRequestURI(), "//", "/");
        long timeConsuming = System.currentTimeMillis() - startTime;
        if (Boolean.TRUE.equals(monitorApiLog)) {
            log.info("request uri: {}, method: {}, timeConsuming: {}, trace_id: {}, span_id: {}",
                    requestUri, httpServletRequest.getMethod(),
                    System.currentTimeMillis() - startTime, traceId, spanId);
        }
        // 指标打点
        apiMonitor.setTraceId(traceId).setSpanId(spanId).setRequestUrl(requestUri)
                .setClientIp(getClientIp(httpServletRequest)).setMethod(httpServletRequest.getMethod())
                .setTimeConsuming(timeConsuming);
        horusEye.mark(MetricsCodes.API_MONITOR, apiMonitor);
    }

    @Override
    public void init(FilterConfig arg0) {
        try {
            ServletContext servletContext = arg0.getServletContext();
            WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            if (ctx == null) {
                this.monitorApiLog = false;
                return;
            }
            String property = ctx.getEnvironment().getProperty("horus.client.monitor-api-log", "false");
            monitorApiLog = BooleanUtil.toBoolean(property);
            // horusEye 注入
            this.horusEye = ctx.getBean(HorusEye.class);
        } catch (Exception e) {
            log.warn("ApiMonitorFilter init error", e);
            monitorApiLog = false;
        }

    }

    /**
     * 获取客户端 IP
     * 
     * @param request
     * @return
     */
    @SuppressWarnings({ "squid:S3776" })
    private String getClientIp(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        try {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = ips[index];
                    if (!("unknown".equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("获取客户端 IP 异常！", e);
            if (StrUtil.isBlank(ip)) {
                ip = "unknown";
            }
        }
        return StrUtil.isBlank(ip) ? "unknown" : ip;
    }

}
