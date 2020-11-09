package com.aa.fly.receipts.data.builder;

import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;

@Component
public class PassengerBuilder implements DataBuilderService {

	@Override
	public void build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {
		
        PassengerDetail passengerDetail = new PassengerDetail();

        passengerDetail.setTicketNumber(ticketReceiptRsRow.getTicketNbr());
        passengerDetail.setFirstName(ticketReceiptRsRow.getFirstNm());
        passengerDetail.setLastName(ticketReceiptRsRow.getLastNm());
        passengerDetail.setAdvantageNumber(ticketReceiptRsRow.getAadvantNbr());
        passengerDetail.setLoyaltyOwnerCode(ticketReceiptRsRow.getLyltyOwnCd());
        
        ticketReceipt.getPassengerDetails().add(passengerDetail);        
	}
	
	// public void setPassengerTotalAmount()
	// public void setShowPassengerTotal()
    // passengerTotalAmount
    // showPassengerTotal

}

