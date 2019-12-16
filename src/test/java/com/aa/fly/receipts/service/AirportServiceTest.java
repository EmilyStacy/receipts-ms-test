package com.aa.fly.receipts.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.domain.AirportLookup;
import com.aa.fly.receipts.domain.AirportLookupObject;
import com.aa.fly.receipts.service.impl.AirportServiceImpl;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class AirportServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    private AirportServiceImpl airportServiceImpl = new AirportServiceImpl();

    @Test
    public void loadAirportMap() throws ParseException {

        airportServiceImpl.setAirportServiceUrl("mockUrl");
        airportServiceImpl.setRestTemplate(restTemplate);

        ResponseEntity<AirportLookupObject> airportLookupEntity = ResponseEntity.ok().body(getAirportLookupObject());
        when(restTemplate.getForEntity("mockUrl", AirportLookupObject.class)).thenReturn(airportLookupEntity);
        airportServiceImpl.loadAirports();

        Airport airport = airportServiceImpl.getAirport("DFW");
        assertEquals("Dallas Fort Worth International", airport.getName());
        assertEquals("DFW", airport.getCode());
        assertEquals("Dallas/Fort Worth", airport.getCity());
        assertEquals("US", airport.getCountryCode());
        assertEquals("TX", airport.getStateCode());

    }

    private AirportLookupObject getAirportLookupObject() {
        AirportLookupObject airportLookupObject = new AirportLookupObject();
        AirportLookup airportLookup = new AirportLookup();
        List<Airport> airportList = new ArrayList<>();
        Airport airport = new Airport();
        airport.setName("Dallas Fort Worth International");
        airport.setCode("DFW");
        airport.setCity("Dallas/Fort Worth");
        airport.setCountryCode("US");
        airport.setStateCode("TX");
        airportList.add(airport);
        airportLookup.setAirportList(airportList);
        airportLookupObject.setAirportLookup(airportLookup);
        return airportLookupObject;
    }
}
