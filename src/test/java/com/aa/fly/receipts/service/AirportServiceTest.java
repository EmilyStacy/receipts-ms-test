package com.aa.fly.receipts.service;

import com.aa.fly.receipts.ReceiptsApplication;
import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.service.impl.AirportServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReceiptsApplication.class)
public class AirportServiceTest {
    @Autowired
    private AirportServiceImpl airportServiceImpl;

    @Test
    public void loadAirportMap() throws ParseException {
        airportServiceImpl.loadAirports();
        Airport airport = airportServiceImpl.getAirport("DFW");
        assertEquals("Dallas Fort Worth International", airport.getName());
        assertEquals("DFW", airport.getCode());
        assertEquals("Dallas/Fort Worth", airport.getCity());
        assertEquals("US", airport.getCountryCode());
        assertEquals("TX", airport.getStateCode());

    }
}
