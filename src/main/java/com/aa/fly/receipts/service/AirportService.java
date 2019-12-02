package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.Airport;

public interface AirportService {

    /**
     * Find the airport data for given airport code
     * 
     * @param airportCode - ticket search criteria
     * @return - instance of Airport
     */
    Airport getAirport(String airportCode);
}
