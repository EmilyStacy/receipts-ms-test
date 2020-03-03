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

    private static final String X_TRANSACTION_ID = "X-Transaction-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        MDC.put("XTransactionID", request.getHeader(X_TRANSACTION_ID));
        response.addHeader(X_TRANSACTION_ID, request.getHeader(X_TRANSACTION_ID));
        return true;
    }
}
