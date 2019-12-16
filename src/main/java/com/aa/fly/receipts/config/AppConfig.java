package com.aa.fly.receipts.config;

import java.util.HashMap;
import java.util.Map;

import com.aa.fly.receipts.interceptor.AddHeadersInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by 629874 on 5/16/2019.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private AddHeadersInterceptor addHeadersInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(addHeadersInterceptor);
    }

    @Bean
    public Map<String, String> fopTypeMap() {
        Map<String, String> fopTypeMap = new HashMap<>();
        fopTypeMap.put("CCAA", "American Airlines Credit Card");
        fopTypeMap.put("CCTP", "UATP");
        fopTypeMap.put("CCJB", "Japan Credit Card");
        fopTypeMap.put("CCJC", "Japan Credit Card");

        return fopTypeMap;
    }
}
