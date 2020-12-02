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
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PassengerAncillaryBuilderTest {

    @InjectMocks
    private PassengerAncillaryBuilder passengerAncillaryBuilder;

    @Test
    public void canaryTest() {
        assertNotNull(passengerAncillaryBuilder);
    }

    @Test
    public void testThatIfMosaicHasAncillariesThenTicketReceiptWillHaveAncillariesAsWell() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ancillaryList));

        Ancillary actualAncillary = ancillaryList.iterator().next();
        assertEquals(Constants.ANCLRY_DOC_NBR, actualAncillary.getAnclryDocNbr());
        assertEquals(Constants.ANCLRY_ISSUE_DATE, actualAncillary.getAnclryIssueDate());
        assertEquals("MAIN CABIN EXTRA (MCO - MIA)", actualAncillary.getAnclryProdName());
        assertEquals(Constants.ANCLRY_PROD_CODE, actualAncillary.getAnclryProdCode());
        assertEquals(Constants.ANCLRY_PRICE_CURRENCY_AMOUNT, actualAncillary.getAnclryPriceCurrencyAmount());
        assertEquals(Constants.ANCLRY_PRICE_CURRENCY_CODE, actualAncillary.getAnclryPriceCurrencyCode());
        assertEquals(Constants.ANCLRY_SALES_CURRENCY_AMOUNT, actualAncillary.getAnclrySalesCurrencyAmount());
        assertEquals(Constants.ANCLRY_SALES_CURRENCY_CODE, actualAncillary.getAnclrySalesCurrencyCode());
        assertEquals("55.69", actualAncillary.getAnclryTaxCurrencyAmount());
    }

    @Test
    public void testIfFopHasNoAncillariesThenTicketReceiptWillAlsoHaveNoAncillaries() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertTrue(CollectionUtils.isEmpty(ancillaryList));
    }

    @Test
    public void testIfAncillaryDocNumberIsNullThenNoAncillariesInFOP() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        ticketReceiptRsRow.setAnclryDocNbr(null);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertTrue(CollectionUtils.isEmpty(ancillaryList));
    }

    @Test
    public void testIfAncillaryDocNumberIsBlankThenNoAncillariesInFOP() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        ticketReceiptRsRow.setAnclryDocNbr("");
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertTrue(CollectionUtils.isEmpty(ancillaryList));
    }

}
