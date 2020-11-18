package com.aa.fly.receipts.config;

import com.aa.ct.fly.logging.MSLoggerAspect;
import org.junit.Assert;
import org.junit.Test;

public class LoggingAspectConfigurationTest {

    @Test
    public void ensureMSLoggerAspectNotNull()
    {
        LoggingAspectConfiguration loggingAspectConfiguration = new LoggingAspectConfiguration();
        MSLoggerAspect msLoggerAspect = loggingAspectConfiguration.notifyAspect();
        Assert.assertNotNull( msLoggerAspect );
    }
}
