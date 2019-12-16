package com.aa.fly.receipts.config;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppConfigTest {

    private AppConfig config = new AppConfig();

    @Test
    public void testFopTypeMap() {
        Map<String, String> fopTypeMap = config.fopTypeMap();
        assertEquals("American Airlines Credit Card", fopTypeMap.get("CCAA"));
        assertEquals("UATP", fopTypeMap.get("CCTP"));
        assertEquals("Japan Credit Card", fopTypeMap.get("CCJB"));
    }
}
