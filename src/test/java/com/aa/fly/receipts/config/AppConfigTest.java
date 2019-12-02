package com.aa.fly.receipts.config;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppConfigTest {

    AppConfig config = new AppConfig();

    @Test
    public void testFopTypeMap() {
        Map<String, String> fopTypeMap = config.fopTypeMap();
        assertEquals("American Express", fopTypeMap.get("CCAX"));
        assertEquals("Diners Club", fopTypeMap.get("CCDC"));
        assertEquals("Discover", fopTypeMap.get("CCDS"));
        assertEquals("Visa", fopTypeMap.get("CCBA"));
        assertEquals("Visa", fopTypeMap.get("CCVI"));
        assertEquals("Master Card", fopTypeMap.get("CCIK"));
        assertEquals("Master Card", fopTypeMap.get("CCMC"));
        assertEquals("Master Card", fopTypeMap.get("CCCA"));
        assertEquals("American Airlines Credit Card", fopTypeMap.get("CCAA"));
        assertEquals("UATP", fopTypeMap.get("CCTP"));
        assertEquals("Japan Credit Card", fopTypeMap.get("CCJP"));
    }
}
