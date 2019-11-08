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
        assertEquals(fopTypeMap.get("CCAX"), "American Express");
        assertEquals(fopTypeMap.get("CCDC"), "Diners Club");
        assertEquals(fopTypeMap.get("CCDS"), "Discover");
        assertEquals(fopTypeMap.get("CCBA"), "Visa");
        assertEquals(fopTypeMap.get("CCVI"), "Visa");
        assertEquals(fopTypeMap.get("CCIK"), "Master Card");
        assertEquals(fopTypeMap.get("CCMC"), "Master Card");
        assertEquals(fopTypeMap.get("CCCA"), "Master Card");
        assertEquals(fopTypeMap.get("CCAA"), "American Airlines Credit Card");
        assertEquals(fopTypeMap.get("CCTP"), "UATP");
        assertEquals(fopTypeMap.get("CCJP"), "Japan Credit Card");
    }
}
