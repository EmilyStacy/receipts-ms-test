package com.aa.fly.receipts.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class WifiReceiptRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private WifiReceiptRepository receiptRepository;

    @Test
    public void findWifiReceipt() throws ParseException {
        WifiSearchCriteria criteria = ReceiptsMSDomainTest.getWifiSearchCriteria();
        WifiReceipt receipt = ReceiptsMSDomainTest.getWifiReceipt();
        when(jdbcTemplate.query(anyString(), any(WifiLineItemMapper.class), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(receipt.getWifiLineItems());
        assertEquals("11.00", receiptRepository.findWifiReceipt(criteria).getWifiLineItems().get(0).getNetPrice());

    }
}
