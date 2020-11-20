package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.data.TaxDescriptionRepository;
import com.aa.fly.receipts.domain.*;
import com.aa.fly.receipts.service.DataBuilderService;
import com.aa.fly.receipts.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class PassengerTaxFeeItemBuilderTest {

    @Mock
    private TicketReceiptRsRow ticketReceiptRsRow;

    @Mock
    private TaxDescriptionRepository taxDescriptionRepository;

    @InjectMocks
    private DataBuilderService passengerTaxFeeItemBuilder = new PassengerTaxFeeItemBuilder();

    private TicketReceipt ticketReceiptMock, ticketReceiptReturn;

    @Test
    public void testBuild_PassengerTaxFeeItemBuilder_NoCityCode() throws Exception {
        this.mockTicketReceipt();

        Mockito.when(taxDescriptionRepository.getDescription(any(), any())).thenReturn("SECURITY SERVICE FEE");
        Mockito.when(ticketReceiptRsRow.getTaxCdSeqId()).thenReturn("1");
        Mockito.when(ticketReceiptRsRow.getTaxCd()).thenReturn("AY");
        Mockito.when(ticketReceiptRsRow.getTaxAmt()).thenReturn("1120");
        Mockito.when(ticketReceiptRsRow.getTaxCurrTypeCd()).thenReturn("USD2");
        Mockito.when(ticketReceiptRsRow.getCityCd()).thenReturn("");
        Mockito.when(ticketReceiptRsRow.getTicketIssueDt()).thenReturn(Date.valueOf("2020-03-04"));

        ticketReceiptReturn = passengerTaxFeeItemBuilder.build(ticketReceiptMock, ticketReceiptRsRow);

        List<Tax> taxes = new ArrayList<>(ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes());

        assertNotNull(ticketReceiptReturn);
        assertNotNull(ticketReceiptReturn.getPassengerDetails());
        assertNotNull(ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees());
        assertEquals(1, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
        assertEquals("1", taxes.get(0).getTaxCodeSequenceId());
        assertEquals("AY", taxes.get(0).getTaxCode());
        assertEquals("SECURITY SERVICE FEE", taxes.get(0).getTaxDescription());
        assertEquals("", taxes.get(0).getCityCode());
        assertEquals("11.20", taxes.get(0).getTaxAmount());
        assertEquals("USD", taxes.get(0).getTaxCurrencyCode());
        assertEquals(11.2, taxes.get(0).getTaxAmountDouble(), 0.01);
    }

    @Test
    public void testBuild_PassengerTaxFeeItemBuilder_WithCityCode() throws Exception {
        this.mockTicketReceipt();

        Mockito.when(taxDescriptionRepository.getDescription(any(), any())).thenReturn("SYS GEN PFC");
        Mockito.when(ticketReceiptRsRow.getTaxCdSeqId()).thenReturn("4");
        Mockito.when(ticketReceiptRsRow.getTaxCd()).thenReturn("XF");
        Mockito.when(ticketReceiptRsRow.getTaxAmt()).thenReturn("450");
        Mockito.when(ticketReceiptRsRow.getTaxCurrTypeCd()).thenReturn("USD2");
        Mockito.when(ticketReceiptRsRow.getCityCd()).thenReturn("STT");
        Mockito.when(ticketReceiptRsRow.getTicketIssueDt()).thenReturn(Date.valueOf("2020-03-04"));

        ticketReceiptReturn = passengerTaxFeeItemBuilder.build(ticketReceiptMock, ticketReceiptRsRow);

        List<Tax> taxes = new ArrayList<>(ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes());

        assertNotNull(ticketReceiptReturn);
        assertNotNull(ticketReceiptReturn.getPassengerDetails());
        assertNotNull(ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees());
        assertEquals(1, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().size());
        assertEquals("4", taxes.get(0).getTaxCodeSequenceId());
        assertEquals("XF", taxes.get(0).getTaxCode());
        assertEquals("SYS GEN PFC (STT)", taxes.get(0).getTaxDescription());
        assertEquals("STT", taxes.get(0).getCityCode());
        assertEquals("4.50", taxes.get(0).getTaxAmount());
        assertEquals("USD", taxes.get(0).getTaxCurrencyCode());
        assertEquals(4.5, taxes.get(0).getTaxAmountDouble(), 0.01);
    }

    private void mockTicketReceipt() throws ParseException {

        ticketReceiptMock = new TicketReceipt();

        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setBaseFareCurrencyCode(Constants.BASE_FARE_CURRENCY_CODE);

        PassengerDetail passengerDetail = new PassengerDetail();
        passengerDetail.setFareTaxesFees(fareTaxesFees);
        passengerDetail.setTicketNumber(Constants.TICKET_NBR);
        passengerDetail.setFirstName(Constants.FIRST_NM);
        passengerDetail.setLastName(Constants.LAST_NM);
        passengerDetail.setAdvantageNumber(Constants.AADVANT_NBR);
        passengerDetail.setLoyaltyOwnerCode(Constants.LYLTY_OWN_CD);

        ticketReceiptMock.getPassengerDetails().add(passengerDetail);
    }
}
