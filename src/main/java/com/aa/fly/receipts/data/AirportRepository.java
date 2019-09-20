package com.aa.fly.receipts.data;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class AirportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    private Map<String, String> airportMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AirportRepository.class);

    // TODO: This load at SpringBoot starting event may not be perfect if the data in Mosaic was ever changed.
    // May be we can also schedule a job to refresh it once a day at midnight?
    // @Transactional(readOnly = true)
    @EventListener(ApplicationReadyEvent.class)
    public void loadAirportMap() {
        String sql = new StringBuilder("SELECT ")
                .append("    AIRPRT_CD, AIRPRT_NM ")
                .append("FROM ").append(ticketSchemaName).append(".AIRPORT_STATION_CURRENT ")
                .toString();

        try {
            airportMap = jdbcTemplate.query(sql, (ResultSetExtractor<Map<String, String>>) resultSet -> {
                while (resultSet.next()) {
                    airportMap.put(resultSet.getString("AIRPRT_CD").trim(), resultSet.getString("AIRPRT_NM"));
                }
                return airportMap;
            });
        } catch (Exception e) {
            // Jenkins doesn't have mosaic connectivity and is failing this step in startup. We can ignore it.
            logger.info("Failed to load airportMap" + e.getMessage());
        }
        logger.info("Loaded " + airportMap.size() + " airport names from Mosaic.");
    }

    public String getAirportName(String airportCode) {
        return airportMap.get(airportCode) == null ? airportCode : airportMap.get(airportCode);
    }

}
