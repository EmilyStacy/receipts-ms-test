package com.aa.fly.receipts.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.aa.fly.receipts.interceptor.AddHeadersInterceptor;

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
        fopTypeMap.put("CA", "Cash / Check");
        fopTypeMap.put("CCAA", "American Airlines Credit Card");
        fopTypeMap.put("CCTP", "UATP");
        fopTypeMap.put("CCJB", "Japan Credit Card");
        fopTypeMap.put("CCJC", "Japan Credit Card");
        fopTypeMap.put("EF", "Exchange");
        fopTypeMap.put("EX", "Exchange");
        return fopTypeMap;
    }

    @Primary
    @Bean(name = "ticketReceipts")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSourceTicketReceipts() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "jdbcTemplateTicketReceipts")
    public JdbcTemplate jdbcTemplateTicketReceipts(@Qualifier("ticketReceipts") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "wifi")
    @ConfigurationProperties(prefix = "spring.datasourcewifi")
    public DataSource dataSourceWifi() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateWifi")
    public JdbcTemplate jdbcTemplateWifi(@Qualifier("wifi") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
