package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.TicketReceipt;

/**
 * Created by 854495 on 11/8/2020.
 */
public interface DataAdjusterService {

    /**
     * Common behavior for all data adjusters
     * 
     * @param ticketReceipt - TicketReceipt will be adjusted by the adjuster.
     * @return TicketReceipt adjusted TicketReceipt
     */
	TicketReceipt adjust(TicketReceipt ticketReceipt);
}
