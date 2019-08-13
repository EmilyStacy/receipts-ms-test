package com.aa.fly.receipts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aa.fly.receipts.data.TicketReceiptRepository;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketSummary;
import com.aa.fly.receipts.service.TicketReceiptService;

/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class TicketReceiptServiceImpl implements TicketReceiptService {

    @Autowired
    private TicketReceiptRepository repository;

    @Override
    public TicketSummary findTicketSummary(SearchCriteria criteria) {
        TicketSummary ticketSummary = null;
        if (StringUtils.hasText(criteria.getTicketNumber())) {
            ticketSummary = repository.findTicketSummaryByTicketNumber(criteria);
        }
        return ticketSummary;
    }

}
