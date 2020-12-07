package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.Ancillary;
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
        assertEquals("0010619785952", actualAncillary.getAnclryDocNbr());
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

    public static void addAncillaryDocNumberToTicketReceiptRowforExcessBaggage(final TicketReceiptRsRow ticketReceiptRsRow) throws ParseException {
        // EXCESS_BAGGAGE
        ticketReceiptRsRow.setAnclryDocNbr("0281234567");
    }

    @Test
    public void testAncillaryDocNumberforExcessBaggage() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        addAncillaryDocNumberToTicketReceiptRowforExcessBaggage(ticketReceiptRsRow);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ancillaryList));
        Ancillary actualAncillary = ancillaryList.iterator().next();
        assertEquals("0010281234567", actualAncillary.getAnclryDocNbr());
    }

    public static void addAncillaryDocNumberToTicketReceiptRowforResFee065(final TicketReceiptRsRow ticketReceiptRsRow) throws ParseException {
        // RES_FEE
        ticketReceiptRsRow.setAnclryDocNbr("0651234567");

    }

    @Test
    public void testAncillaryDocNumberforResFee065() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        addAncillaryDocNumberToTicketReceiptRowforResFee065(ticketReceiptRsRow);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ancillaryList));
        Ancillary actualAncillary = ancillaryList.iterator().next();
        assertEquals("0010651234567", actualAncillary.getAnclryDocNbr());
    }


    public static void addAncillaryDocNumberToTicketReceiptRowforMainExtraCabin(final TicketReceiptRsRow ticketReceiptRsRow) throws ParseException {
        // MAIN_EXTRA_CABIN
        ticketReceiptRsRow.setAnclryDocNbr("0621234567");
    }

    @Test
    public void testAncillaryDocNumberforMainExtraCabin() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        addAncillaryDocNumberToTicketReceiptRowforMainExtraCabin(ticketReceiptRsRow);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ancillaryList));
        Ancillary actualAncillary = ancillaryList.iterator().next();
        assertEquals("0010621234567", actualAncillary.getAnclryDocNbr());
    }

    public static void addAncillaryDocNumberToTicketReceiptRowforStdPaidSeat(final TicketReceiptRsRow ticketReceiptRsRow) throws ParseException {
        // STD_PAID_SEAT
        ticketReceiptRsRow.setAnclryDocNbr("0651234567");

    }

    @Test
    public void testAncillaryDocNumberforStdPaidSeat() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        addAncillaryDocNumberToTicketReceiptRowforStdPaidSeat(ticketReceiptRsRow);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ancillaryList));
        Ancillary actualAncillary = ancillaryList.iterator().next();
        assertEquals("0010651234567", actualAncillary.getAnclryDocNbr());
    }

    public static void addAncillaryDocNumberToTicketReceiptRowforPrefferredSeat(final TicketReceiptRsRow ticketReceiptRsRow) throws ParseException {
        // PREFERRED_SEAT
        ticketReceiptRsRow.setAnclryDocNbr("0611234567");

    }

    @Test
    public void testAncillaryDocNumberforPrefferredSeat() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        addAncillaryDocNumberToTicketReceiptRowforPrefferredSeat(ticketReceiptRsRow);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ancillaryList));
        Ancillary actualAncillary = ancillaryList.iterator().next();
        assertEquals("0010611234567", actualAncillary.getAnclryDocNbr());
    }

    public static void addAncillaryDocNumberToTicketReceiptRowforResFee061(final TicketReceiptRsRow ticketReceiptRsRow) throws ParseException {
        // RES_FEE
        ticketReceiptRsRow.setAnclryDocNbr("0611234567");
    }

    @Test
    public void testAncillaryDocNumberforResFee061() throws ParseException {
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        addAncillaryDocNumberToTicketReceiptRowforResFee061(ticketReceiptRsRow);
        Set<Ancillary> ancillaryList = passengerAncillaryBuilder.build(ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ancillaryList));
        Ancillary actualAncillary = ancillaryList.iterator().next();
        assertEquals("0010611234567", actualAncillary.getAnclryDocNbr());
    }


}
