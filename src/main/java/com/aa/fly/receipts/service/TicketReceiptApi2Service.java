package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.TicketReceipt;
import org.springframework.http.ResponseEntity;


public interface TicketReceiptApi2Service {
    ResponseEntity<TicketReceipt> findTicketReceipt(SearchCriteriaApi2 criteria);
}
