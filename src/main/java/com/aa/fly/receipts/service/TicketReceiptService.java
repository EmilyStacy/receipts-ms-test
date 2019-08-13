package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketSummary;

/**
 * Created by 629874 on 5/9/2019.
 */
public interface TicketReceiptService {

    /**
     * Find the ticket summary data for given search criteria
     * 
     * @param criteria - ticket search criteria
     * @return - instance of TicketSummary
     */
    TicketSummary findTicketSummary(SearchCriteria criteria);
}
