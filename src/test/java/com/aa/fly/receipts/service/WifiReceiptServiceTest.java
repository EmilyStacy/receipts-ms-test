package com.aa.fly.receipts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.aa.fly.receipts.data.WifiReceiptRepository;
import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.impl.WifiReceiptServiceImpl;

/**
 * Created by 629874 on 5/9/2019.
 */

@RunWith(MockitoJUnitRunner.class)
public class WifiReceiptServiceTest {
    @InjectMocks
    private WifiReceiptServiceImpl wifiReceiptService;

    @Mock
    private WifiReceiptRepository wifiReceiptRepository;

    @Test
    public void testWifiReceiptTotal() throws ParseException {
        WifiReceipt expectedReceipt = ReceiptsMSDomainTest.getWifiReceipt();
        when(wifiReceiptRepository.findWifiReceipt(any())).thenReturn(expectedReceipt);
        WifiReceipt actualReceipt = wifiReceiptService.findWifiReceipt(new WifiSearchCriteria());
        assertThat(actualReceipt.getWifiLineItems()).isNotNull();
        assertThat(actualReceipt.getWifiLineItems().size()).isEqualTo(1);
        assertThat(actualReceipt.getWifiLineItems().get(0).getNetPrice()).isEqualTo("11.00");
    }

}
