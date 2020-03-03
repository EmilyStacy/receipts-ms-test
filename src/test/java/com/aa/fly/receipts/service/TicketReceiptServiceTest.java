package com.aa.fly.receipts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.aa.fly.receipts.data.TicketReceiptRepository;
import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.NoCostDetailsFoundException;
import com.aa.fly.receipts.service.impl.TicketReceiptServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TicketReceiptServiceTest {
    @InjectMocks
    private TicketReceiptServiceImpl ticketReceiptService;

    @Mock
    private TicketReceiptRepository ticketReceiptRepository;

    @Test
    public void testTicketReceiptTotal() throws ParseException {
        TicketReceipt expectedReceipt = ReceiptsMSDomainTest.getTicketReceipt();
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        when(ticketReceiptRepository.findTicketReceiptByTicketNumber(criteria)).thenReturn(expectedReceipt);
        when(ticketReceiptRepository.findCostDetailsByTicketNumber(criteria, expectedReceipt.getPassengerDetails().get(0))).thenReturn(expectedReceipt.getPassengerDetails().get(0));
        TicketReceipt actualReceipt = ticketReceiptService.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNotNull();
        assertThat(actualReceipt.getAirlineAccountCode()).isEqualTo("001");
        assertThat(actualReceipt.getPnr()).isEqualTo("MRYMPT");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount()).isEqualTo("77674");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode()).isEqualTo("USD2");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount()).isEqualTo("84930");
    }

    @Test
    public void findTicketReceipt_shouldReturnNoCostDetailsFoundStatusMessage() throws ParseException {
        TicketReceipt expectedReceipt = ReceiptsMSDomainTest.getTicketReceipt();
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        when(ticketReceiptRepository.findTicketReceiptByTicketNumber(criteria)).thenReturn(expectedReceipt);
        when(ticketReceiptRepository.findCostDetailsByTicketNumber(criteria, expectedReceipt.getPassengerDetails().get(0))).thenThrow(NoCostDetailsFoundException.class);
        TicketReceipt actualReceipt = ticketReceiptService.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNotNull();
        assertThat(actualReceipt.getAirlineAccountCode()).isEqualTo("001");
        assertThat(actualReceipt.getPnr()).isEqualTo("MRYMPT");
        assertThat(actualReceipt.getStatusMessage()).isEqualTo("NoCostDetailsFound");
    }

    @Test
    public void verifyTicketAirlineCode_validAirlineCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SearchCriteria sc = new SearchCriteria();
        sc.setTicketNumber("0011234567890");

        Method method = ticketReceiptService.getClass().getDeclaredMethod("verifyTicketAirlineCode", SearchCriteria.class);
        method.setAccessible(true);

        method.invoke(ticketReceiptService, sc);
        assertThat(sc.getTicketNumber()).isEqualTo("0011234567890");
    }

    @Test
    public void verifyTicketAirlineCode_shouldPrependTicketNumberWith001() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SearchCriteria sc = new SearchCriteria();
        sc.setTicketNumber("0021234567890");

        Method method = ticketReceiptService.getClass().getDeclaredMethod("verifyTicketAirlineCode", SearchCriteria.class);
        method.setAccessible(true);

        method.invoke(ticketReceiptService, sc);
        assertThat(sc.getTicketNumber()).isEqualTo("0011234567890");
    }
}
