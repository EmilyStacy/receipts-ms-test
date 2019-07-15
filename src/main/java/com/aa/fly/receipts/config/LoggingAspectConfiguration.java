package com.aa.fly.receipts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aa.ct.fly.logging.MSLoggerAspect;

@Configuration
public class LoggingAspectConfiguration {
    @Bean
    public MSLoggerAspect notifyAspect() {
        return new MSLoggerAspect();
    }
}