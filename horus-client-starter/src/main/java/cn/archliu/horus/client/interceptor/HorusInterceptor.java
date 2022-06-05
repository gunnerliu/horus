package cn.archliu.horus.client.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yomahub.tlog.constant.TLogConstants;
import com.yomahub.tlog.context.TLogContext;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author: Arch
 * @Date: 2022-05-04 09:24:48
 * @Description: 拦截器
 */
public class HorusInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 把spanId放入response的header
        response.addHeader(TLogConstants.TLOG_SPANID_KEY, TLogContext.getSpanId());
        return true;
    }

}
