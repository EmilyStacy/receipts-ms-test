package com.aa.fly.receipts;

import org.junit.Assert;
import org.junit.Test;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfigTest {

    @Test
    public void ensureDocketNotNull()
    {
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        Docket docket = swaggerConfig.api();
        Assert.assertNotNull( docket );
    }
}
