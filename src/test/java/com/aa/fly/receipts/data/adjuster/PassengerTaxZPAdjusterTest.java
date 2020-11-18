package com.aa.fly.receipts.data.adjuster;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.aa.fly.receipts.domain.Tax;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;

/* Test cases
 * #1: No ZP (should not touch)
 * #2: 1 ZP (should not touch)
 * #3: 2 ZPs 
 * 		- same amount for both (should not touch)
 * 		- One amount higher than the other (keep higher amount entry and drop the other one)
 * #4: 3 ZPs
 * 		- same amount for all 3 (should not touch)
 * 		- One amount equals to sum of the other two (keep higher amount entry and drop the other two)
 * #5: 4 ZPs
 * 		- same amount for all 4 (should not touch)
 * 		- One amount equals to sum of the others (keep higher amount entry and drop the others)
 */
public class PassengerTaxZPAdjusterTest {
	
    private PassengerTaxZPAdjuster passengerTaxZPAdjuster = new PassengerTaxZPAdjuster();
    private TicketReceipt ticketReceiptMock, ticketReceiptReturn;

	@Test
	public void testAdjustNoZP() throws Exception {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	
    	ticketReceiptReturn = passengerTaxZPAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(1, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
	}

	@Test
	public void testAdjustOneZP() throws Exception {
    	ticketReceiptMock = Utils.mockTicketReceipt();
        Tax tax = new Tax();    	
        tax.setTaxCodeSequenceId("2");
        tax.setTaxCode("ZP");
        tax.setTaxDescription("");

    	ticketReceiptReturn = passengerTaxZPAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(1, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
	}

}
