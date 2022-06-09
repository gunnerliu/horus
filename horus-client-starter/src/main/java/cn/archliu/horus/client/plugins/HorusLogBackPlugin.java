package cn.archliu.horus.client.plugins;

import com.yomahub.tlog.context.TLogContext;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import cn.archliu.horus.client.metrics.entity.LogMonitor;
import cn.archliu.horus.client.metrics.enums.MetricsCodes;
import cn.archliu.horus.client.monitor.HorusEye;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @Author: Arch
 * @Date: 2022-05-14 18:35:32
 * @Description: logback
 */
@Component("horusLogBackPlugin")
@ConditionalOnProperty(prefix = "horus.client", name = "monitor-logback", havingValue = "true", matchIfMissing = false)
public class HorusLogBackPlugin {

    protected HorusLogBackPlugin(HorusEye horusEye) {
        HorusTurboFilter horusTurboFilter = new HorusTurboFilter(horusEye);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.addTurboFilter(horusTurboFilter);
        loggerContext.addListener(new LoggerContextListener() {
            @Override
            public boolean isResetResistant() {
                return true;
            }

            @Override
            public void onReset(LoggerContext context) {
                loggerContext.addTurboFilter(horusTurboFilter);
            }

            @Override
            public void onStart(LoggerContext context) {
                // no-op
            }

            @Override
            public void onStop(LoggerContext context) {
                // no-op
            }

            @Override
            public void onLevelChange(Logger logger, Level level) {
                // no-op
            }
        });
    }

    public static class HorusTurboFilter extends TurboFilter {

        private HorusEye horusEye;

        public HorusTurboFilter(HorusEye horusEye) {
            this.horusEye = horusEye;
        }

        @Override
        public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params,
                Throwable t) {
            // error 日志异步发布事件
            if (Level.ERROR.equals(level) && isMark(format, params, t)) {
                try {

                    LoggingEvent le = new LoggingEvent(Logger.class.getName(), logger, level, format, t, params);
                    LogMonitor logMonitor = new LogMonitor().setLogger(le.getLoggerName())
                            .setMessage(le.getFormattedMessage()).setTraceId(TLogContext.getTraceId())
                            .setSpanId(TLogContext.getSpanId());
                    if (le.getThrowableProxy() != null) {
                        logMonitor.setThrowName(le.getThrowableProxy().getClassName())
                                .setThrowMsg(le.getThrowableProxy().getMessage());
                    }
                    horusEye.mark(MetricsCodes.LOG_MONITOR, logMonitor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return FilterReply.NEUTRAL;
        }

        /**
         * 如果错误信息、错误参数、异常都为空的话，不进行打点
         * 
         * @param message
         * @param params
         * @param t
         * @return
         */
        private boolean isMark(String message, Object[] params, Throwable t) {
            return StrUtil.isNotBlank(message) && ArrayUtil.isNotEmpty(params) && t != null;
        }

    }

}
