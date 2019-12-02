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
    public static final String MASTER_CARD = "Master Card";
    public static final String VISA = "Visa";
    @Autowired
    private AddHeadersInterceptor addHeadersInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(addHeadersInterceptor);
    }

    @Bean
    public Map<String, String> fopTypeMap() {
        Map<String, String> fopTypeMap = new HashMap<>();
        fopTypeMap.put("CCAX", "American Express");
        fopTypeMap.put("CCDC", "Diners Club");
        fopTypeMap.put("CCDS", "Discover");
        fopTypeMap.put("CCBA", VISA);
        fopTypeMap.put("CCVI", VISA);
        fopTypeMap.put("CCIK", MASTER_CARD);
        fopTypeMap.put("CCMC", MASTER_CARD);
        fopTypeMap.put("CCCA", MASTER_CARD);
        fopTypeMap.put("CCAA", "American Airlines Credit Card");
        fopTypeMap.put("CCTP", "UATP");
        fopTypeMap.put("CCJP", "Japan Credit Card");
        return fopTypeMap;
    }
}
