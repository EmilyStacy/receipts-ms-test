package com.aa.fly.receipts.data.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.AirportService;
import com.aa.fly.receipts.service.DataBuilderService;

@Component
public class PnrHeaderBuilder implements DataBuilderService {
	
	@Autowired
	private AirportService airportService;

	@Override
	public void build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {
		ticketReceipt.setAirlineAccountCode(ticketReceiptRsRow.getAirlnAcctCd());
		ticketReceipt.setTicketIssueDate(ticketReceiptRsRow.getTicketIssueDt());
		ticketReceipt.setDepartureDate(ticketReceiptRsRow.getDepDt());
		ticketReceipt.setPnr(ticketReceiptRsRow.getPnr());		
        ticketReceipt.setOriginAirport(airportService.getAirport(ticketReceiptRsRow.getOrgAtoCd()));
        ticketReceipt.setDestinationAirport(airportService.getAirport(ticketReceiptRsRow.getDestAtoCd()));
	}
}
