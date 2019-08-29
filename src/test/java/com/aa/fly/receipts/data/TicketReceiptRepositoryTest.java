package com.aa.fly.receipts.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketReceiptRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TicketReceiptRepository receiptRepository;

    @Test
    public void findWifiReceipt() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        TicketReceipt ticketReceipt = ReceiptsMSDomainTest.getTicketSummary();
        List<TicketReceipt> ticketReceiptList = new ArrayList<>();
        ticketReceiptList.add(ticketReceipt);
        when(jdbcTemplate.query(anyString(), any(TicketReceiptMapper.class), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(ticketReceiptList);
        assertEquals("MRYMPT", receiptRepository.findTicketReceiptByTicketNumber(criteria).getPnr());

    }
}
