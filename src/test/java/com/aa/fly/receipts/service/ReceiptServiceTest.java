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

import com.aa.fly.receipts.data.ReceiptRepository;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.impl.ReceiptServiceImpl;

/**
 * Created by 629874 on 5/9/2019.
 */

@RunWith(MockitoJUnitRunner.class)
public class ReceiptServiceTest {
    @InjectMocks
    private ReceiptServiceImpl receiptService;

    @Mock
    private ReceiptRepository receiptRepository;

    @Test
    public void testReceiptTotal() {
        Receipt expectedReceipt = getReceipt();
        when(receiptRepository.findReceipt(any())).thenReturn(expectedReceipt);
        Receipt actualReceipt = receiptService.findReceipt(new SearchCriteria());
        assertEquals(expectedReceipt.getFirstName(), actualReceipt.getFirstName());
        assertEquals(expectedReceipt.getLastName(), actualReceipt.getLastName());
        assertEquals(expectedReceipt.getReceiptTotal(), actualReceipt.getReceiptTotal());
    }

    @Test
    public void testWifiReceiptTotal() throws ParseException {
        WifiReceipt expectedReceipt = ReceiptsMSDomainTest.getWifiReceipt();
        when(receiptRepository.findWifiReceipt(any())).thenReturn(expectedReceipt);
        WifiReceipt actualReceipt = receiptService.findWifiReceipt(new WifiSearchCriteria());
        assertThat(actualReceipt.getWifiLineItems()).isNotNull();
        assertThat(actualReceipt.getWifiLineItems().size()).isEqualTo(1);
        assertThat(actualReceipt.getWifiLineItems().get(0).getNetPrice()).isEqualTo("11.00");
    }

    private Receipt getReceipt() {
        Receipt receipt = new Receipt();
        receipt.setReceiptTotal("239.00");
        receipt.setLastName("LastName");
        receipt.setFirstName("FirstName");
        return receipt;
    }

}
