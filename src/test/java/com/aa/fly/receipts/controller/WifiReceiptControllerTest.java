package com.aa.fly.receipts.controller;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.WifiReceiptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)

public class WifiReceiptControllerTest {
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

    public static SearchCriteriaApi2 getSearchCriteria() {
        SearchCriteriaApi2 searchCriteriaApi2 = new SearchCriteriaApi2();
        searchCriteriaApi2.setLastName("last");
        searchCriteriaApi2.setTicketNumber("1234");
        return searchCriteriaApi2;
    }
}
