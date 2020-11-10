package com.aa.fly.receipts.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.aa.fly.receipts.exception.BulkTicketException;
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
        TicketReceipt actualReceipt = ticketReceiptService.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNotNull();
        assertThat(actualReceipt.getAirlineAccountCode()).isEqualTo("001");
        assertThat(actualReceipt.getPnr()).isEqualTo("MRYMPT");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount()).isEqualTo("77674");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode()).isEqualTo("USD2");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount()).isEqualTo("84930");
    }
    
    @Test
    public void testTicketReceiptBulkTicketException() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        when(ticketReceiptRepository.findTicketReceiptByTicketNumber(criteria)).thenThrow(BulkTicketException.class);
        
        TicketReceipt actualReceipt = ticketReceiptService.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNotNull();
        assertThat(actualReceipt.getStatusMessage()).isEqualTo("BulkTicket");
    }

    @Test
    public void testCriteriaTicketNumber10Chars() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        criteria.setTicketNumber("1234567890");
        TicketReceipt actualReceipt = ticketReceiptService.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNull();
    }

    @Test
    public void verifyTicketAirlineCode_validAirlineCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SearchCriteria sc = new SearchCriteria();
        sc.setTicketNumber("0011234567890");

        Method method = ticketReceiptService.getClass().getDeclaredMethod("verifyTicketAirlineCode", SearchCriteria.class);
        method.setAccessible(true);

        method.invoke(ticketReceiptService, sc);
        assertThat(sc.getTicketNumber()).isEqualTo("1234567890");
    }

    @Test
    public void verifyTicketAirlineCode_shouldPrependTicketNumberWith001() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SearchCriteria sc = new SearchCriteria();
        sc.setTicketNumber("0021234567890");

        Method method = ticketReceiptService.getClass().getDeclaredMethod("verifyTicketAirlineCode", SearchCriteria.class);
        method.setAccessible(true);

        method.invoke(ticketReceiptService, sc);
        assertThat(sc.getTicketNumber()).isEqualTo("1234567890");
    }


}
