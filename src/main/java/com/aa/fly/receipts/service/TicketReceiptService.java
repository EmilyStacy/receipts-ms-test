package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import org.springframework.http.ResponseEntity;

/**
 * Created by 629874 on 5/9/2019.
 */
public interface TicketReceiptService {

    /**
     * Find the ticket receipt data for given search criteria
     * 
     * @param criteria - ticket search criteria
     * @return - instance of TicketReceipt
     */
    ResponseEntity<TicketReceipt> findTicketReceipt(SearchCriteria criteria);
}
