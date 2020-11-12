package com.aa.fly.receipts.data.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import com.aa.fly.receipts.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
public class PassengerBuilderTest {

    private DataBuilderService passengerBuilder = new PassengerBuilder();
    
    private TicketReceipt ticketReceipt = new TicketReceipt();
    private TicketReceiptRsRow ticketReceiptRsRow = null;
	
	@Test
	public void testBuild_Passenger() throws Exception {
		this.mockTicketReceiptRsRow();
		
		this.ticketReceipt = passengerBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);
		
		assertNotNull(this.ticketReceipt);
		assertNotNull(this.ticketReceipt.getPassengerDetails());
		assertEquals(1, this.ticketReceipt.getPassengerDetails().size());
		assertEquals(Constants.TICKET_NBR, this.ticketReceipt.getPassengerDetails().get(0).getTicketNumber());
		assertEquals(Constants.FIRST_NM, this.ticketReceipt.getPassengerDetails().get(0).getFirstName());
		assertEquals(Constants.LAST_NM, this.ticketReceipt.getPassengerDetails().get(0).getLastName());
		assertEquals(Constants.AADVANT_NBR, this.ticketReceipt.getPassengerDetails().get(0).getAdvantageNumber());
		assertEquals(Constants.LYLTY_OWN_CD, this.ticketReceipt.getPassengerDetails().get(0).getLoyaltyOwnerCode());
	}
		
	private void mockTicketReceiptRsRow() throws ParseException {
		this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
		.ticketNbr(Constants.TICKET_NBR)
		.firstNm(Constants.FIRST_NM)
		.lastNm(Constants.LAST_NM)
		.aadvantNbr(Constants.AADVANT_NBR)
		.lyltyOwnCd(Constants.LYLTY_OWN_CD)
		.build();
	}
}
