package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;

public interface AirportService {

    /**
     * Find the airport data for given airport code
     * 
     * @param airportCode - ticket search criteria
     * @return - instance of Airport
     */
    Airport getAirport(String airportCode);
}
