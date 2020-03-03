package com.aa.fly.receipts.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by 629874 on 5/16/2019.
 */
@Component
public class AddHeadersInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        MDC.put("XTransactionID", request.getHeader("X-Transaction-ID"));
        response.addHeader("X-Transaction-ID", request.getHeader("X-Transaction-ID"));
        return true;
    }
}
