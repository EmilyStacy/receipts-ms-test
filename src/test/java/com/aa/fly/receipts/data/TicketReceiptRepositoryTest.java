package com.aa.fly.receipts.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.aa.fly.receipts.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.util.Utils;

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
    private TicketReceiptRsExtracter ticketReceiptRsExtracter;

    @InjectMocks
    private TicketReceiptRepository receiptRepository;

    private List<TicketReceiptRsRow> ticketReceiptRsRowList = new ArrayList<>();
    
    @Test
    public void findTicketReceiptByTicketNumber() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        TicketReceipt ticketReceipt = ReceiptsMSDomainTest.getTicketReceipt();
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();        
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(ticketReceiptRsExtracter.extract(resultSet))
        .thenReturn(ticketReceiptRsRowList);
        when(ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList))
        .thenReturn(ticketReceipt);
        
        assertEquals("MRYMPT", receiptRepository.findTicketReceiptByTicketNumber(criteria).getPnr());
        assertEquals("USED", receiptRepository.findTicketReceiptByTicketNumber(criteria).getSegmentDetails().get(0).getSegmentStatus());
    }

    @Test
    public void findTicketReceiptByTicketNumberInSearchCriteriaApi2() throws ParseException {
        SearchCriteriaApi2 criteria = ReceiptsMSDomainTest.getSearchCriteriaApi2WithTicketNumber();
        TicketReceipt ticketReceipt = ReceiptsMSDomainTest.getTicketReceipt();
        TicketReceiptRsRow ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        ticketReceiptRsRowList.add(ticketReceiptRsRow);

        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(ticketReceiptRsExtracter.extract(resultSet))
                .thenReturn(ticketReceiptRsRowList);
        when(ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList))
                .thenReturn(ticketReceipt);

        assertEquals("MRYMPT", receiptRepository.findTicketReceiptByTicketNumber(criteria).getPnr());
        assertEquals("USED", receiptRepository.findTicketReceiptByTicketNumber(criteria).getSegmentDetails().get(0).getSegmentStatus());
    }

    @Test
    public void findTicketReceiptByTicketNumberDataNotFound() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(ticketReceiptRsExtracter.extract(resultSet))
        .thenReturn(ticketReceiptRsRowList);
        when(ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList))
                .thenReturn(new TicketReceipt());
        
        assertNotNull(receiptRepository.findTicketReceiptByTicketNumber(criteria));
    }

    @Test
    public void findTicketReceiptByTicketNumberDataNotFoundForSearchCriteriaApi2() throws ParseException {
        SearchCriteriaApi2 criteria = ReceiptsMSDomainTest.getSearchCriteriaApi2WithTicketNumber();
        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(ticketReceiptRsExtracter.extract(resultSet))
                .thenReturn(ticketReceiptRsRowList);
        when(ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList))
                .thenReturn(new TicketReceipt());

        assertNotNull(receiptRepository.findTicketReceiptByTicketNumber(criteria));
    }
}
