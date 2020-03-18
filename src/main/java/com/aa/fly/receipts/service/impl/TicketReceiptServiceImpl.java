package com.aa.fly.receipts.service.impl;

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
import com.aa.fly.receipts.exception.StatusMessage;
/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class TicketReceiptServiceImpl implements TicketReceiptService {

    @Autowired
    private TicketReceiptRepository repository;

    private static String AIRLINE_CODE = "001";

    @Override
    public ResponseEntity<TicketReceipt> findTicketReceipt(SearchCriteria criteria) {

        verifyTicketAirlineCode(criteria);

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
            } catch (NoCostDetailsFoundException e) {
                ticketReceipt.setStatusMessage(StatusMessage.NO_COST.getStatusMessage());
                ticketReceiptResponse = ResponseEntity.status(HttpStatus.OK).body(ticketReceipt);
            }
        } else {
            ticketReceiptResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body(new TicketReceipt());
        }

        return ticketReceiptResponse;
    }

    private void verifyTicketAirlineCode(SearchCriteria criteria) {
        String ticketNumberSc = criteria.getTicketNumber();
        String ticketNumberLast10 = (ticketNumberSc.length() == 13) ? ticketNumberSc.substring(3) : ticketNumberSc;
        criteria.setTicketNumber(AIRLINE_CODE.concat(ticketNumberLast10));
    }

}
