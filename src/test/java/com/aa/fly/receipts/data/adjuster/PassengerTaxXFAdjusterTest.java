package com.aa.fly.receipts.data.adjuster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.aa.fly.receipts.domain.Tax;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.util.Utils;

/* Test cases
 * #1: Base currency USD &Tax currency USD 
 * #2: No XF (should not touch)
 * #3: Base currency USD &Tax currency CAD
 * #4: Base currency EUR &Tax currency EUR
 */
public class PassengerTaxXFAdjusterTest {

	private PassengerTaxXFAdjuster passengerTaxXFAdjuster = new PassengerTaxXFAdjuster();
	private TicketReceipt ticketReceiptMock, ticketReceiptReturn;
	// XF tax amount always comes as USD, even though baseFareCurrency is not USD.
	// If baseFareCurrencyCode is not USD, merge all XFs into one entry
	// calculate the XF amount by adding the base fare, taxes with baseFareCurrency
	// and subtract the amount from totalFare.
	// We need to do this so all the line items add up to the total amount on the
	// receipt.

	@Test
	public void testBuild_PassengerTax_USD_XF_BaseCurrencyAmt_USD() throws Exception {

		ticketReceiptMock = Utils.mockTicketReceipt();

		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setTotalFareAmount("484.90");
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareAmount("409.30");
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareCurrencyCode("USD");

		Tax xfTax = new Tax();
		xfTax.setTaxCode("XF");
		xfTax.setTaxAmount("4.50");
		xfTax.setTaxCodeSequenceId("3");
		xfTax.setTaxCurrencyCode("USD");

		Tax xfTax2 = new Tax();
		xfTax2.setTaxCode("XF");
		xfTax2.setTaxAmount("4.50");
		xfTax2.setTaxCodeSequenceId("4");
		xfTax2.setTaxCurrencyCode("USD");

		Tax xfTax3 = new Tax();
		xfTax3.setTaxCode("XF");
		xfTax3.setTaxAmount("4.50");
		xfTax3.setTaxCodeSequenceId("5");
		xfTax3.setTaxCurrencyCode("USD");

		Tax xfTax4 = new Tax();
		xfTax4.setTaxCode("XF");
		xfTax4.setTaxAmount("3.00");
		xfTax4.setTaxCodeSequenceId("6");
		xfTax4.setTaxCurrencyCode("USD");

		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(xfTax);
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(xfTax2);
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(xfTax3);
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(xfTax4);
		ticketReceiptReturn = passengerTaxXFAdjuster.adjust(ticketReceiptMock);

		int xfTaxItems = (int) ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().stream()
				.filter(t -> "XF".equals(t.getTaxCode())).count();
		Tax adjustedTax = ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().stream()
				.filter(t -> "XF".equals(t.getTaxCode())).findAny().orElseThrow(null);
		assertEquals(4, xfTaxItems);
		assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
		assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("USD");
		assertThat(adjustedTax.getTaxAmount()).isEqualTo("4.50");
	}

	@Test
	public void testBuild_PassengerTaxNo_XF_BaseCurrencyAmt_USD() throws Exception {
		ticketReceiptMock = Utils.mockTicketReceipt();
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setTotalFareAmount("22.08");
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareAmount("16.00");
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareCurrencyCode("USD");

		ticketReceiptMock = Utils.mockTicketReceipt();

		ticketReceiptReturn = passengerTaxXFAdjuster.adjust(ticketReceiptMock);

		assertEquals(1, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
		int xfTaxItems = (int) ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().stream()
				.filter(t -> "XF".equals(t.getTaxCode())).count();
		assertEquals(0, xfTaxItems);
	}

	@Test
	public void testBuild_PassengerTax_CAD_XF_BaseCurrencyAmt_USD() throws Exception {
		ticketReceiptMock = Utils.mockTicketReceipt();
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setTotalFareAmount("1039.60");
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareAmount("700");
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareCurrencyCode("USD");

		Tax xfTax1 = new Tax();
		xfTax1.setTaxCode("XF");
		xfTax1.setTaxAmount("75.00");
		xfTax1.setTaxCodeSequenceId("2");
		xfTax1.setTaxCurrencyCode("CAD");

		Tax xfTax2 = new Tax();
		xfTax2.setTaxCode("XF");
		xfTax2.setTaxAmount("30.00");
		xfTax2.setTaxCodeSequenceId("3");
		xfTax2.setTaxCurrencyCode("CAD");

		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(xfTax1);
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(xfTax2);
		ticketReceiptReturn = passengerTaxXFAdjuster.adjust(ticketReceiptMock);

		int xfTaxItems = (int) ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().stream()
				.filter(t -> "XF".equals(t.getTaxCode())).count();
		Tax adjustedTax = ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().stream()
				.filter(t -> "XF".equals(t.getTaxCode())).findAny().orElseThrow(null);
		assertEquals(2, xfTaxItems);
		assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
		assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("CAD");
		assertThat(adjustedTax.getTaxAmount()).isEqualTo("75.00");

	}

	@Test
	public void testBuild_PassengerTax_EUR_XF_BaseCurrencyAmt_EUR() throws Exception {

		ticketReceiptMock = Utils.mockTicketReceipt();
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().setBaseFareCurrencyCode("EUR");
		Tax xfTax = new Tax();
		xfTax.setTaxCode("XF");
		xfTax.setTaxAmount("3.80");
		xfTax.setTaxCodeSequenceId("2");
		xfTax.setTaxCurrencyCode("EUR");
		ticketReceiptMock.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(xfTax);
		ticketReceiptReturn = passengerTaxXFAdjuster.adjust(ticketReceiptMock);
		int xfTaxItems = (int) ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().stream()
				.filter(t -> "XF".equals(t.getTaxCode())).count();

		Tax adjustedTax = ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().stream()
				.filter(t -> "XF".equals(t.getTaxCode())).findAny().orElseThrow(null);
		assertEquals(1, xfTaxItems);
		assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
		assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("EUR");
		assertThat(adjustedTax.getTaxAmount()).isEqualTo("3.80");
	}

}
