package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import com.aa.fly.receipts.util.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class PassengerFareTaxFeeBuilderTest {

    @Mock
    private TicketReceiptRsRow ticketReceiptRsRow;

    @InjectMocks
    private DataBuilderService passengerFareTaxFeeBuilder = new PassengerFareTaxFeeBuilder();

    private TicketReceipt ticketReceiptMock, ticketReceiptReturn;

    @Test
    public void testBuild_PassengerFareTaxFee_EqfnFareAmt_Zero() throws Exception {
        this.mockTicketReceipt();

        Mockito.when(ticketReceiptRsRow.getEqfnFareAmt()).thenReturn("0");
        Mockito.when(ticketReceiptRsRow.getEqfnFareCurrTypeCd()).thenReturn("");
        Mockito.when(ticketReceiptRsRow.getFnumFareAmt()).thenReturn("39442");
        Mockito.when(ticketReceiptRsRow.getFnumFareCurrTypeCd()).thenReturn("USD2");
        Mockito.when(ticketReceiptRsRow.getFareTdamAmt()).thenReturn("45280");

        this.ticketReceiptReturn = passengerFareTaxFeeBuilder.build(ticketReceiptMock, ticketReceiptRsRow);

        assertNotNull(ticketReceiptReturn);
        assertNotNull(ticketReceiptReturn.getPassengerDetails());
        assertEquals(1, ticketReceiptReturn.getPassengerDetails().size());
        assertNotNull(ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees());
        assertEquals("394.42", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount());
        assertEquals("USD", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode());
        assertEquals("452.80", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
        assertEquals("58.38", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxFareAmount());
    }

    @Test
    public void testBuild_PassengerFareTaxFee_EqfnFareAmt_Null() throws Exception {
        this.mockTicketReceipt();

        Mockito.when(ticketReceiptRsRow.getEqfnFareAmt()).thenReturn(null);
        Mockito.when(ticketReceiptRsRow.getEqfnFareCurrTypeCd()).thenReturn(null);
        Mockito.when(ticketReceiptRsRow.getFnumFareAmt()).thenReturn("39442");
        Mockito.when(ticketReceiptRsRow.getFnumFareCurrTypeCd()).thenReturn("USD2");
        Mockito.when(ticketReceiptRsRow.getFareTdamAmt()).thenReturn("45280");

        this.ticketReceiptReturn = passengerFareTaxFeeBuilder.build(ticketReceiptMock, ticketReceiptRsRow);

        assertNotNull(ticketReceiptReturn);
        assertNotNull(ticketReceiptReturn.getPassengerDetails());
        assertEquals(1, ticketReceiptReturn.getPassengerDetails().size());
        assertNotNull(ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees());
        assertEquals("394.42", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount());
        assertEquals("USD", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode());
        assertEquals("452.80", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
        assertEquals("58.38", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxFareAmount());
    }

    @Test
    public void testBuild_PassengerFareTaxFee_EqfnFareAmt_NotZero() throws Exception {
        this.mockTicketReceipt();

        Mockito.when(ticketReceiptRsRow.getEqfnFareAmt()).thenReturn("9657");
        Mockito.when(ticketReceiptRsRow.getEqfnFareCurrTypeCd()).thenReturn("DOP0");
        Mockito.when(ticketReceiptRsRow.getFnumFareAmt()).thenReturn("19100");
        Mockito.when(ticketReceiptRsRow.getFnumFareCurrTypeCd()).thenReturn("USD2");
        Mockito.when(ticketReceiptRsRow.getFareTdamAmt()).thenReturn("15919");

        this.ticketReceiptReturn = passengerFareTaxFeeBuilder.build(ticketReceiptMock, ticketReceiptRsRow);

        assertNotNull(ticketReceiptReturn);
        assertNotNull(ticketReceiptReturn.getPassengerDetails());
        assertEquals(1, ticketReceiptReturn.getPassengerDetails().size());
        assertNotNull(ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees());
        assertEquals("9657.00", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount());
        assertEquals("DOP", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode());
        assertEquals("15919.00", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
        assertEquals("6262.00", ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxFareAmount());
    }


    private void mockTicketReceipt() throws ParseException {

        ticketReceiptMock = new TicketReceipt();
        PassengerDetail passengerDetail = new PassengerDetail();
        passengerDetail.setTicketNumber(Constants.TICKET_NBR);
        passengerDetail.setFirstName(Constants.FIRST_NM);
        passengerDetail.setLastName(Constants.LAST_NM);
        passengerDetail.setAdvantageNumber(Constants.AADVANT_NBR);
        passengerDetail.setLoyaltyOwnerCode(Constants.LYLTY_OWN_CD);

        ticketReceiptMock.getPassengerDetails().add(passengerDetail);
    }
}
