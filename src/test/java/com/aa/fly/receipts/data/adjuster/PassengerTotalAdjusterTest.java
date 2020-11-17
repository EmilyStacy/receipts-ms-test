package com.aa.fly.receipts.data.adjuster;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;

/* Test cases
 * #1: ticket fop only - amt 0, 50.23
 * #2: ticket fop + 1 anc fop (same currency)
 * #3: ticket fop + 1 anc fop (different currency than ticket)
 * #4: ticket fop + 2 anc fops (same currency)
 * #5: ticket fop + 2 anc fops (one of them different currency than ticket)
 */
public class PassengerTotalAdjusterTest {
	
    private PassengerTotalAdjuster passengerTotalAdjuster = new PassengerTotalAdjuster();
    private TicketReceipt ticketReceiptMock, ticketReceiptReturn;

	@Test
	public void testAdjustTicketFopOnly() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	
    	ticketReceiptReturn = passengerTotalAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(Constants.FOP_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals(Constants.FOP_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getPassengerTotalAmount());
	}

	@Test
	public void testAdjustTicketFopOnlyZeroFopAmount() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopAmount("0");
    	
    	ticketReceiptReturn = passengerTotalAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals("0", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals(Constants.TOTAL_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getPassengerTotalAmount());
	}

	@Test
	public void testAdjustTicketFopPlusOneOtherFopWithSameCurrency() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(Constants.dateFormat.parse("2020-10-12"));
        formOfPayment.setFopTypeCode(Constants.FOP_TYPE_CODE);
        formOfPayment.setFopTypeDescription(Constants.FOP_TYPE_DESCRIPTION);
        formOfPayment.setFopAccountNumberLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4);
        formOfPayment.setFopAmount("10.00");
        formOfPayment.setFopCurrencyCode(Constants.FOP_CURRENCY_CODE);
    	
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(formOfPayment);
    	
    	ticketReceiptReturn = passengerTotalAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(Constants.FOP_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals("10.00", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(1).getFopAmount());
    	assertEquals("462.80", ticketReceiptReturn.getPassengerDetails().get(0).getPassengerTotalAmount());
    	assertEquals(true, ticketReceiptReturn.getPassengerDetails().get(0).isShowPassengerTotal());
	}
	
	@Test
	public void testAdjustTicketFopPlusOneOtherFopWithDiffCurrency() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareCurrencyCode("EUR");;
    	
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(Constants.dateFormat.parse("2020-10-12"));
        formOfPayment.setFopTypeCode(Constants.FOP_TYPE_CODE);
        formOfPayment.setFopTypeDescription(Constants.FOP_TYPE_DESCRIPTION);
        formOfPayment.setFopAccountNumberLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4);
        formOfPayment.setFopAmount("10.00");
        formOfPayment.setFopCurrencyCode("USD");
        
        Utils.addOneAncillary(formOfPayment);
    	
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(formOfPayment);
    	
    	ticketReceiptReturn = passengerTotalAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(Constants.FOP_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals("10.00", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(1).getFopAmount());
    	assertEquals("462.80", ticketReceiptReturn.getPassengerDetails().get(0).getPassengerTotalAmount());
    	assertEquals(false, ticketReceiptReturn.getPassengerDetails().get(0).isShowPassengerTotal());
	}

	@Test
	public void testAdjustTicketFopPlusTwoOtherFopsWithSameCurrency() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(Constants.dateFormat.parse("2020-10-12"));
        formOfPayment.setFopTypeCode(Constants.FOP_TYPE_CODE);
        formOfPayment.setFopTypeDescription(Constants.FOP_TYPE_DESCRIPTION);
        formOfPayment.setFopAccountNumberLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4);
        formOfPayment.setFopAmount("10.00");
        formOfPayment.setFopCurrencyCode(Constants.FOP_CURRENCY_CODE);
    	
        Utils.addOneAncillary(formOfPayment);

    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(formOfPayment);
    	
        formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(Constants.dateFormat.parse("2020-10-14"));
        formOfPayment.setFopTypeCode(Constants.FOP_TYPE_CODE);
        formOfPayment.setFopTypeDescription(Constants.FOP_TYPE_DESCRIPTION);
        formOfPayment.setFopAccountNumberLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4);
        formOfPayment.setFopAmount("20.00");
        formOfPayment.setFopCurrencyCode(Constants.FOP_CURRENCY_CODE);
    	
        Utils.addOneAncillary(formOfPayment);

    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(formOfPayment);
    	
    	ticketReceiptReturn = passengerTotalAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(Constants.FOP_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals("10.00", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(1).getFopAmount());
    	assertEquals("20.00", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(2).getFopAmount());
    	assertEquals("482.80", ticketReceiptReturn.getPassengerDetails().get(0).getPassengerTotalAmount());
    	assertEquals(true, ticketReceiptReturn.getPassengerDetails().get(0).isShowPassengerTotal());
	}
	
	@Test
	public void testAdjustTicketFopPlusTwoOtherFopsWithDiffCurrency() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareCurrencyCode("EUR");;
    	
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(Constants.dateFormat.parse("2020-10-12"));
        formOfPayment.setFopTypeCode(Constants.FOP_TYPE_CODE);
        formOfPayment.setFopTypeDescription(Constants.FOP_TYPE_DESCRIPTION);
        formOfPayment.setFopAccountNumberLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4);
        formOfPayment.setFopAmount("10.00");
        formOfPayment.setFopCurrencyCode("USD");
        
        Utils.addOneAncillary(formOfPayment);
    	
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(formOfPayment);
    	
        formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(Constants.dateFormat.parse("2020-10-12"));
        formOfPayment.setFopTypeCode(Constants.FOP_TYPE_CODE);
        formOfPayment.setFopTypeDescription(Constants.FOP_TYPE_DESCRIPTION);
        formOfPayment.setFopAccountNumberLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4);
        formOfPayment.setFopAmount("20.00");
        formOfPayment.setFopCurrencyCode("USD");
        
        Utils.addOneAncillary(formOfPayment);
    	
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(formOfPayment);
    	
    	ticketReceiptReturn = passengerTotalAdjuster.adjust(ticketReceiptMock);
    	
    	assertEquals(Constants.FOP_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals("10.00", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(1).getFopAmount());
    	assertEquals("20.00", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(2).getFopAmount());
    	assertEquals("482.80", ticketReceiptReturn.getPassengerDetails().get(0).getPassengerTotalAmount());
    	assertEquals(false, ticketReceiptReturn.getPassengerDetails().get(0).isShowPassengerTotal());
	}	
}
