package com.aa.fly.receipts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.TicketReceiptService;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)

public class TicketReceiptControllerTest {
    @Mock
    private TicketReceiptService receiptService;

    @InjectMocks
    private TicketReceiptController controller;

    @Test
    public void findWifiReceipt() throws Exception {
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
}
