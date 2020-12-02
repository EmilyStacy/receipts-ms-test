package com.aa.fly.receipts.data.adjuster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.aa.fly.receipts.domain.Tax;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;

/* Test cases
 * #1: No ZP (should not touch)
 * #2: 1 ZP (should not touch)
 * #3: 2 ZPs 
 * 		- same amount for both (keep one of them)
 * 		- One amount higher than the other (keep both)
 * #4: 3 ZPs
 * 		- same amount for all 3 (keep all 3)
 * 		- One amount equals to sum of the other two (keep higher amount entry and drop the other two)
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
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        
    	ticketReceiptReturn = passengerTaxZPAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(2, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
    	for(Tax iTax: ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes()) {
    	    Tax localTax = iTax;
    	    if (localTax.getTaxCode().equals("ZP")) {
    	    	assertEquals("8.60", localTax.getTaxAmount());
    	    }
    	}
	}

	@Test
	public void testAdjustTwoZPsSameAmount() throws Exception {
    	ticketReceiptMock = Utils.mockTicketReceipt();
        Tax tax = new Tax();    	
        tax.setTaxCodeSequenceId("2");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        tax = new Tax();    	
        tax.setTaxCodeSequenceId("3");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        
    	ticketReceiptReturn = passengerTaxZPAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(2, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
    	for(Tax iTax: ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes()) {
    	    Tax localTax = iTax;
    	    if (localTax.getTaxCode().equals("ZP")) {
    	    	assertEquals("8.60", localTax.getTaxAmount());
    	    }
    	}
	}

	@Test
	public void testAdjustTwoZPsOneAmountHigher() throws Exception {
    	ticketReceiptMock = Utils.mockTicketReceipt();
        Tax tax = new Tax();    	
        tax.setTaxCodeSequenceId("2");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("4.30");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        tax = new Tax();    	
        tax.setTaxCodeSequenceId("3");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        
    	ticketReceiptReturn = passengerTaxZPAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(3, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
    	for(Tax iTax: ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes()) {
    	    Tax localTax = iTax;
    	    if (localTax.getTaxCode().equals("ZP")) {
    	    	assertTrue(localTax.getTaxAmount().equals("4.30") ||
    	    			localTax.getTaxAmount().equals("8.60"));    	    
    	    }
    	}
	}
	
	@Test
	public void testAdjustThreeZPsSameAmount() throws Exception {
    	ticketReceiptMock = Utils.mockTicketReceipt();
        Tax tax = new Tax();    	
        tax.setTaxCodeSequenceId("2");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        tax = new Tax();    	
        tax.setTaxCodeSequenceId("3");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        tax = new Tax();    	
        tax.setTaxCodeSequenceId("4");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        
    	ticketReceiptReturn = passengerTaxZPAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(4, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
    	for(Tax iTax: ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes()) {
    	    Tax localTax = iTax;
    	    if (localTax.getTaxCode().equals("ZP")) {
    	    	assertEquals("8.60", localTax.getTaxAmount());
    	    }
    	}
	}
	
	@Test
	public void testAdjustThreeZPsOneAmountHigher() throws Exception {
    	ticketReceiptMock = Utils.mockTicketReceipt();
        Tax tax = new Tax();    	
        tax.setTaxCodeSequenceId("2");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("4.30");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        tax = new Tax();    	
        tax.setTaxCodeSequenceId("3");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("4.30");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        tax = new Tax();    	
        tax.setTaxCodeSequenceId("4");
        tax.setTaxCode("ZP");
        tax.setTaxAmount("8.60");
        ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        
    	ticketReceiptReturn = passengerTaxZPAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(2, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
    	for(Tax iTax: ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes()) {
    	    Tax localTax = iTax;
    	    if (localTax.getTaxCode().equals("ZP")) {
    	    	assertEquals("8.60", localTax.getTaxAmount());
    	    }
    	}
	}
}
