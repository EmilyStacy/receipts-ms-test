package com.aa.fly.receipts.data;

import com.aa.fly.receipts.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketReceiptRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SqlRowSet resultSet;

    @Mock
    private TicketReceiptMapper ticketReceiptMapper;

    @Mock
    private CostDetailsMapper costDetailsMapper;

    @InjectMocks
    private TicketReceiptRepository receiptRepository;

    @Test
    public void findTicketReceiptByTicketNumber() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        TicketReceipt ticketReceipt = ReceiptsMSDomainTest.getTicketReceipt();
        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(ticketReceiptMapper.mapTicketReceipt(resultSet))
                .thenReturn(ticketReceipt);
        assertEquals("MRYMPT", receiptRepository.findTicketReceiptByTicketNumber(criteria).getPnr());
        assertEquals("USED", receiptRepository.findTicketReceiptByTicketNumber(criteria).getSegmentDetails().get(0).getSegmentStatus());
    }

    @Test
    public void findTicketReceiptByTicketNumberDataNotFound() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(ticketReceiptMapper.mapTicketReceipt(resultSet))
                .thenReturn(null);
        assertNull(receiptRepository.findTicketReceiptByTicketNumber(criteria));
    }
}
