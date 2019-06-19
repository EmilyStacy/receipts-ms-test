package com.aa.fly.receipts.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 629874 on 5/16/2019.
 */
@Component
public class AddHeadersInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AddHeadersInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("In preHandle method of Interceptor");
        response.addHeader("X-Transaction-ID", request.getHeader("X-Transaction-ID"));
        return true;
    }
}
