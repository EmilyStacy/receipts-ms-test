package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PassengerAncillaryBuilderTest {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @InjectMocks
    private PassengerAncillaryBuilder passengerAncillaryBuilder;

    @Test
    public void canaryTest() {
        assertNotNull(passengerAncillaryBuilder);
    }

    @Test
    public void testThatIfMosaicHasNoAncillariesThenTicketReceiptHasNoAncillariesAsWell() throws ParseException {
        TicketReceipt ticketReceipt = Utils.mockTicketReceipt();
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        Utils.addOneAncillary(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0));
        ticketReceipt = passengerAncillaryBuilder.build(ticketReceipt, ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getAncillaries()));

        Ancillary actualAncillary = ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getAncillaries().iterator().next();
        assertEquals(Constants.ANCLRY_DOC_NBR, actualAncillary.getAnclryDocNbr());
        assertEquals(Constants.ANCLRY_ISSUE_DATE, actualAncillary.getAnclryIssueDate());
        assertEquals(Constants.ANCLRY_PROD_NAME, actualAncillary.getAnclryProdName());
        assertEquals(Constants.ANCLRY_PROD_CODE, actualAncillary.getAnclryProdCode());
        assertEquals(Constants.ANCLRY_PRICE_CURRENCY_AMOUNT, actualAncillary.getAnclryPriceCurrencyAmount());
        assertEquals(Constants.ANCLRY_PRICE_CURRENCY_CODE, actualAncillary.getAnclryPriceCurrencyCode());
        assertEquals(Constants.ANCLRY_SALES_CURRENCY_AMOUNT, actualAncillary.getAnclrySalesCurrencyAmount());
        assertEquals(Constants.ANCLRY_SALES_CURRENCY_CODE, actualAncillary.getAnclrySalesCurrencyCode());
        assertEquals(Constants.ANCLRY_TAX_CURRENCY_AMOUNT, actualAncillary.getAnclryTaxCurrencyAmount());
    }

}
