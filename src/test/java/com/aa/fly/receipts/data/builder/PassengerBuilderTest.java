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

@RunWith(SpringJUnit4ClassRunner.class)
public class PassengerBuilderTest {

    private DataBuilderService passengerBuilder = new PassengerBuilder();
    
	static final String TICKET_NBR = "2128785282";
	static final String FIRST_NM = "JAXON";
	static final String LAST_NM = "SHAW";
	static final String AADVANT_NBR = "1A2B3C";
	static final String LYLTY_OWN_CD = "AA";

    private TicketReceipt ticketReceipt = new TicketReceipt();
    private TicketReceiptRsRow ticketReceiptRsRow = null;
	
	@Test
	public void testBuild_Passenger() throws Exception {
		this.mockTicketReceiptRsRow();
		
		passengerBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);
		
		assertNotNull(this.ticketReceipt);
		assertNotNull(this.ticketReceipt.getPassengerDetails());
		assertEquals(1, this.ticketReceipt.getPassengerDetails().size());
		assertEquals(TICKET_NBR, this.ticketReceipt.getPassengerDetails().get(0).getTicketNumber());
		assertEquals(FIRST_NM, this.ticketReceipt.getPassengerDetails().get(0).getFirstName());
		assertEquals(LAST_NM, this.ticketReceipt.getPassengerDetails().get(0).getLastName());
		assertEquals(AADVANT_NBR, this.ticketReceipt.getPassengerDetails().get(0).getAdvantageNumber());
		assertEquals(LYLTY_OWN_CD, this.ticketReceipt.getPassengerDetails().get(0).getLoyaltyOwnerCode());
	}
		
	private void mockTicketReceiptRsRow() throws ParseException {
		this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
		.ticketNbr(TICKET_NBR)
		.firstNm(FIRST_NM)
		.lastNm(LAST_NM)
		.aadvantNbr(AADVANT_NBR)
		.lyltyOwnCd(LYLTY_OWN_CD)
		.build();
	}
}
