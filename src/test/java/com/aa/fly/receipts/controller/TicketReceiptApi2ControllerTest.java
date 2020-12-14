package com.aa.fly.receipts.controller;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.TicketReceiptApi2Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)

public class TicketReceiptApi2ControllerTest {
    @Mock
    TicketReceiptApi2Service ticketReceiptApi2Service;

    @Mock
    private UncategorizedSQLException uncategorizedSQLException;

    @InjectMocks
    TicketReceiptApi2Controller ticketReceiptApi2Controller;

    @Test
    public void findTicketReceipt() throws Exception {
        final SearchCriteriaApi2 searchCriteria = ReceiptsMSDomainTest.getSearchCriteriaApi2WithTicketNumber();
        final ResponseEntity<TicketReceipt> receiptExpected = ResponseEntity.ok().body(ReceiptsMSDomainTest.getTicketReceipt());

        when(ticketReceiptApi2Service.findTicketReceipt(any())).thenReturn(receiptExpected);

        TicketReceipt receiptActual = ticketReceiptApi2Controller.getTicketReceipt(searchCriteria).getBody();

        assertThat(receiptActual).isNotNull();
        assertThat(receiptActual.getAirlineAccountCode()).isEqualTo("001");
        assertThat(receiptActual.getPnr()).isEqualTo("MRYMPT");
    }

    @Test
    public void testUncategorizedSQLException(){

        Mockito.when(uncategorizedSQLException.getMessage()).thenReturn(null);
        TicketReceipt ticketReceipt = ticketReceiptApi2Controller.uncategorizedSQLException(uncategorizedSQLException);
        assertEquals("UncategorizedSQLException", ticketReceipt.getStatusMessage());

        Mockito.when(uncategorizedSQLException.getMessage()).thenReturn("none");
        ticketReceipt = ticketReceiptApi2Controller.uncategorizedSQLException(uncategorizedSQLException);
        assertEquals("UncategorizedSQLException", ticketReceipt.getStatusMessage());

        Mockito.when(uncategorizedSQLException.getMessage()).thenReturn("SocketTimeoutException");
        ticketReceipt = ticketReceiptApi2Controller.uncategorizedSQLException(uncategorizedSQLException);
        assertEquals("QueryTimeout", ticketReceipt.getStatusMessage());
    }
}
