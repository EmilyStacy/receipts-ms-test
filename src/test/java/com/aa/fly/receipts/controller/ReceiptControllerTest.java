package com.aa.fly.receipts.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.WifiReceiptService;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)

public class ReceiptControllerTest {
    @Mock
    private WifiReceiptService receiptService;

    @InjectMocks
    private WifiReceiptController controller;

    @Test
    public void findWifiReceipt() throws Exception {
        final WifiSearchCriteria searchCriteria = ReceiptsMSDomainTest.getWifiSearchCriteria();
        final WifiReceipt receiptExpected = ReceiptsMSDomainTest.getWifiReceipt();

        when(receiptService.findWifiReceipt(any())).thenReturn(receiptExpected);

        WifiReceipt receiptAcutal = controller.getWifiReceipts(searchCriteria);

        assertThat(receiptAcutal.getWifiLineItems()).isNotNull();
        assertThat(receiptAcutal.getWifiLineItems().size()).isEqualTo(1);
        assertThat(receiptAcutal.getWifiLineItems().get(0).getNetPrice()).isEqualTo("11.00");

    }

    public static SearchCriteria getSearchCriteria() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setDepartureDate(new Date());
        searchCriteria.setFirstName("first");
        searchCriteria.setLastName("last");
        searchCriteria.setTicketNumber("1234");
        return searchCriteria;
    }
}
