package com.aa.fly.receipts.service.impl;

import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.domain.AirportLookup;
import com.aa.fly.receipts.domain.AirportLookupObject;
import com.aa.fly.receipts.service.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AirportServiceImpl implements AirportService {

    @Value("${airport.service.url}")
    private String airportServiceUrl;

    private Map<String, Airport> airportMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AirportServiceImpl.class);

    @Scheduled(cron = "0 0 2 * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void loadAirports() {
        try {
            logger.info("Loading airports........");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<AirportLookupObject> airportLookupEntity = restTemplate.getForEntity(airportServiceUrl, AirportLookupObject.class);
            if (HttpStatus.OK.equals(airportLookupEntity.getStatusCode())) {
                AirportLookup airportLookup = airportLookupEntity.getBody().getAirportLookup();
                if (airportLookup != null && !CollectionUtils.isEmpty(airportLookup.getAirportList())) {
                    airportMap.clear();
                    airportLookup.getAirportList().stream().forEach(airport -> airportMap.put(airport.getCode(), airport));
                    logger.info("Loaded " + airportMap.size() + " airports from airport lookup service");
                } else {
                    logger.error("Zero airports received from airport lookup service!!!");
                }
            } else {
                logger.error("Airports lookup failed with status code " + airportLookupEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occured while doing airport lookup", e);
        }
    }

    @Override
    public Airport getAirport(String airportCode) {
        return airportMap.get(airportCode);
    }

}
