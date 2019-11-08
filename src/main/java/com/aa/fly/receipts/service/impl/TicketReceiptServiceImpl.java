package com.aa.fly.receipts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aa.fly.receipts.data.TicketReceiptRepository;
import com.aa.fly.receipts.domain.FormOfPayment;
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

        if(ticketReceipt.getPnr() != null) {
            List<FormOfPayment> formOfPayments = repository.findCostDetailsByTicketNumber(criteria);
            if(formOfPayments != null && formOfPayments.size() > 0) {
                ticketReceipt.getPassengerDetails().get(0).setFormOfPayments(formOfPayments);
            }
        }
        return ticketReceipt;
    }

}
