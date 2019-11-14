package com.aa.fly.receipts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aa.fly.receipts.data.TicketReceiptRepository;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.TicketReceiptService;

/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class TicketReceiptServiceImpl implements TicketReceiptService {

    @Autowired
    private TicketReceiptRepository repository;

    @Override
    public TicketReceipt findTicketReceipt(SearchCriteria criteria) {
        TicketReceipt ticketReceipt = null;
        if (StringUtils.hasText(criteria.getTicketNumber())) {
            ticketReceipt = repository.findTicketReceiptByTicketNumber(criteria);
        }

        if (ticketReceipt != null && ticketReceipt.getPnr() != null) {
            PassengerDetail passengerDetail = repository.findCostDetailsByTicketNumber(criteria, ticketReceipt.getPassengerDetails().get(0));
            ticketReceipt.getPassengerDetails().set(0, passengerDetail);
        }

        return ticketReceipt;
    }

}
