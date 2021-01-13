package com.aa.fly.receipts.service.impl;

import com.aa.fly.receipts.data.TicketReceiptRepository;
import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.AgencyTicketException;
import com.aa.fly.receipts.exception.BulkTicketException;
import com.aa.fly.receipts.exception.StatusMessage;
import com.aa.fly.receipts.manager.AgencyTicketManager;
import com.aa.fly.receipts.service.TicketReceiptApi2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TicketReceiptApi2ServiceImpl implements TicketReceiptApi2Service {

    @Autowired
    private TicketReceiptRepository repository;
    
    @Autowired
    private AgencyTicketManager agencyTicketManager;

    @Override
    public ResponseEntity<TicketReceipt> findTicketReceipt(SearchCriteriaApi2 criteria) {

        verifyTicketAirlineCode(criteria);

        ResponseEntity<TicketReceipt> ticketReceiptResponse = null;
        TicketReceipt ticketReceipt = null;

        if (StringUtils.hasText(criteria.getTicketNumber())) {
            try {
                ticketReceipt = repository.findTicketReceiptByTicketNumber(criteria);

                if (ticketReceipt != null && StringUtils.hasText(ticketReceipt.getPnr())) {
                    ticketReceiptResponse = ResponseEntity.ok().body(ticketReceipt);
                } else {
                	agencyTicketManager.check(criteria.getTicketNumber());
                	                	
                    ticketReceiptResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body(ticketReceipt);
                }
            }
            catch( BulkTicketException e ) {
                ticketReceipt = new TicketReceipt();

                ticketReceipt.setStatusMessage(StatusMessage.BULK_TICKET.getStatusMessage());
                ticketReceiptResponse = ResponseEntity.status(HttpStatus.OK).body(ticketReceipt);
            }
            catch( AgencyTicketException e ) {
                ticketReceipt = new TicketReceipt();
                
                ticketReceipt.setStatusMessage(StatusMessage.AGENCY_TICKET.getStatusMessage());
                ticketReceiptResponse = ResponseEntity.status(HttpStatus.OK).body(ticketReceipt);
            }
        }

        return ticketReceiptResponse;
    }

    private void verifyTicketAirlineCode(SearchCriteriaApi2 criteria) {
        String ticketNumberSc = criteria.getTicketNumber().trim();
        String ticketNumberLast10 = (ticketNumberSc.length() == 13) ? ticketNumberSc.substring(3) : ticketNumberSc;
        criteria.setTicketNumber(ticketNumberLast10);
    }
}
