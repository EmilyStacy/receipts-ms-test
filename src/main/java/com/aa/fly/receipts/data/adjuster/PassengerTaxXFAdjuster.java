package com.aa.fly.receipts.data.adjuster;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.DataAdjusterService;

public class PassengerTaxXFAdjuster implements DataAdjusterService {

	@Override
	public TicketReceipt adjust(TicketReceipt ticketReceipt) {

		return ticketReceipt;
	}
}
