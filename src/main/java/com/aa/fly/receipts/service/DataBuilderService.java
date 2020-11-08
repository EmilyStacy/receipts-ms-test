package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;

/**
 * Created by 854495 on 11/8/2020.
 */
public interface DataBuilderService {

    /**
     * Common behavior for all data builders
     * 
     * @param ticketReceipt - TicketReceipt response will be built by the builder.
     * @param ticketReceiptRsRow - TicketReceiptRsRow element of corresponding result set row
     * which TicketReceipt response will get built from.
     */
    void build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow);
}
