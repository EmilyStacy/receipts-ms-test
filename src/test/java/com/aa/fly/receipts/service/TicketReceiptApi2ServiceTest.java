package com.aa.fly.receipts.service;

import com.aa.fly.receipts.data.TicketReceiptRepository;
import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.BulkTicketException;
import com.aa.fly.receipts.service.impl.TicketReceiptApi2ServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketReceiptApi2ServiceTest {
    @InjectMocks
    private TicketReceiptApi2ServiceImpl ticketReceiptApi2Service;

    @Mock
    private TicketReceiptRepository ticketReceiptRepository;

    @Test
    public void testTicketReceiptTotal() throws ParseException {
        TicketReceipt expectedReceipt = ReceiptsMSDomainTest.getTicketReceipt();
        SearchCriteriaApi2 criteria = ReceiptsMSDomainTest.getSearchCriteriaApi2WithTicketNumber();
        when(ticketReceiptRepository.findTicketReceiptByTicketNumber(criteria)).thenReturn(expectedReceipt);
        TicketReceipt actualReceipt = ticketReceiptApi2Service.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNotNull();
        assertThat(actualReceipt.getAirlineAccountCode()).isEqualTo("001");
        assertThat(actualReceipt.getPnr()).isEqualTo("MRYMPT");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount()).isEqualTo("77674");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode()).isEqualTo("USD2");
        assertThat(actualReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount()).isEqualTo("84930");
    }
    
    @Test
    public void testTicketReceiptBulkTicketException() throws ParseException {
        SearchCriteriaApi2 criteria = ReceiptsMSDomainTest.getSearchCriteriaApi2WithTicketNumber();
        when(ticketReceiptRepository.findTicketReceiptByTicketNumber(criteria)).thenThrow(BulkTicketException.class);
        
        TicketReceipt actualReceipt = ticketReceiptApi2Service.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNotNull();
        assertThat(actualReceipt.getStatusMessage()).isEqualTo("BulkTicket");
    }

    @Test
    public void testCriteriaTicketNumber10Chars() throws ParseException {
        SearchCriteriaApi2 criteria = ReceiptsMSDomainTest.getSearchCriteriaApi2WithTicketNumber();
        criteria.setTicketNumber("1234567890");
        TicketReceipt actualReceipt = ticketReceiptApi2Service.findTicketReceipt(criteria).getBody();
        assertThat(actualReceipt).isNull();
    }

    @Test
    public void verifyTicketAirlineCode_validAirlineCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SearchCriteriaApi2 sc = new SearchCriteriaApi2();
        sc.setTicketNumber("0011234567890");

        Method method = ticketReceiptApi2Service.getClass().getDeclaredMethod("verifyTicketAirlineCode", SearchCriteriaApi2.class);
        method.setAccessible(true);

        method.invoke(ticketReceiptApi2Service, sc);
        assertThat(sc.getTicketNumber()).isEqualTo("1234567890");
    }

    @Test
    public void verifyTicketAirlineCode_shouldPrependTicketNumberWith001() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SearchCriteriaApi2 sc = new SearchCriteriaApi2();
        sc.setTicketNumber("0021234567890");

        Method method = ticketReceiptApi2Service.getClass().getDeclaredMethod("verifyTicketAirlineCode", SearchCriteriaApi2.class);
        method.setAccessible(true);

        method.invoke(ticketReceiptApi2Service, sc);
        assertThat(sc.getTicketNumber()).isEqualTo("1234567890");
    }


}
