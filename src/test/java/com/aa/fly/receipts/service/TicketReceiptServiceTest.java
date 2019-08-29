package com.aa.fly.receipts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import com.aa.fly.receipts.service.impl.TicketReceiptServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TicketReceiptServiceTest {
    @InjectMocks
    private TicketReceiptServiceImpl ticketReceiptService;

    @Mock
    private TicketReceiptRepository ticketReceiptRepository;

    @Test
    public void testWifiReceiptTotal() throws ParseException {
        TicketReceipt expectedReceipt = ReceiptsMSDomainTest.getTicketSummary();
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        when(ticketReceiptRepository.findTicketReceiptByTicketNumber(criteria)).thenReturn(expectedReceipt);
        TicketReceipt actualReceipt = ticketReceiptService.findTicketReceipt(criteria);
        assertThat(actualReceipt).isNotNull();
        assertThat(actualReceipt.getAirlineAccountCode()).isEqualTo("001");
        assertThat(actualReceipt.getPnr()).isEqualTo("MRYMPT");
    }
}