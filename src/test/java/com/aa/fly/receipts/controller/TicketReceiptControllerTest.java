package com.aa.fly.receipts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.TicketReceiptService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)

public class TicketReceiptControllerTest {
    @Mock
    private TicketReceiptService receiptService;

    @Mock
    private UncategorizedSQLException uncategorizedSQLException;

    @InjectMocks
    private TicketReceiptController controller;

    @Test
    public void findTicketReceipt() throws Exception {
        final SearchCriteria searchCriteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        final ResponseEntity<TicketReceipt> receiptExpected = ResponseEntity.ok().body(ReceiptsMSDomainTest.getTicketReceipt());

        when(receiptService.findTicketReceipt(any())).thenReturn(receiptExpected);

        TicketReceipt receiptActual = controller.getTicketReceipt(searchCriteria).getBody();

        assertThat(receiptActual).isNotNull();
        assertThat(receiptActual.getAirlineAccountCode()).isEqualTo("001");
        assertThat(receiptActual.getPnr()).isEqualTo("MRYMPT");
    }

    public static SearchCriteria getSearchCriteria() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setDepartureDate("09/30/2019");
        searchCriteria.setFirstName("first");
        searchCriteria.setLastName("last");
        searchCriteria.setTicketNumber("1234");
        return searchCriteria;
    }

    @Test
    public void testUncategorizedSQLException(){

        Mockito.when(uncategorizedSQLException.getMessage()).thenReturn(null);
        TicketReceipt ticketReceipt = controller.uncategorizedSQLException(uncategorizedSQLException);
        assertEquals("UncategorizedSQLException", ticketReceipt.getStatusMessage());

        Mockito.when(uncategorizedSQLException.getMessage()).thenReturn("none");
        ticketReceipt = controller.uncategorizedSQLException(uncategorizedSQLException);
        assertEquals("UncategorizedSQLException", ticketReceipt.getStatusMessage());

        Mockito.when(uncategorizedSQLException.getMessage()).thenReturn("SocketTimeoutException");
        ticketReceipt = controller.uncategorizedSQLException(uncategorizedSQLException);
        assertEquals("QueryTimeout", ticketReceipt.getStatusMessage());
    }
}
