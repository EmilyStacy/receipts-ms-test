package com.aa.fly.receipts.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aa.fly.receipts.data.TicketReceiptRepository;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.NoCostDetailsFoundException;
import com.aa.fly.receipts.service.TicketReceiptService;

import sun.security.krb5.internal.Ticket;

/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class TicketReceiptServiceImpl implements TicketReceiptService {

    private static final Logger logger = LoggerFactory.getLogger(TicketReceiptServiceImpl.class);
    @Autowired
    private TicketReceiptRepository repository;

    @Override
    public ResponseEntity<TicketReceipt> findTicketReceipt(SearchCriteria criteria) {
        ResponseEntity<TicketReceipt> ticketReceiptResponse = null;
        TicketReceipt ticketReceipt = null;
        if (StringUtils.hasText(criteria.getTicketNumber())) {
            ticketReceipt = repository.findTicketReceiptByTicketNumber(criteria);
        }

        if (ticketReceipt != null && StringUtils.hasText(ticketReceipt.getPnr())) {
            try {
                PassengerDetail passengerDetail = repository.findCostDetailsByTicketNumber(criteria, ticketReceipt.getPassengerDetails().get(0));
                ticketReceipt.getPassengerDetails().set(0, passengerDetail);
                ticketReceiptResponse = ResponseEntity.ok().body(ticketReceipt);
            }catch (NoCostDetailsFoundException e) {
                logger.error("No cost details found for search criteria = \" + criteria", e.getClass().getName());
                logger.error("Error details = ", e);
                ticketReceipt.setStatusMessage("NoCostDetailsFound");
                ticketReceiptResponse = ResponseEntity.status(HttpStatus.OK).body(ticketReceipt);
            }
        } else {
            ticketReceiptResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body(new TicketReceipt());
        }

        return ticketReceiptResponse;
    }

}
