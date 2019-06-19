package com.aa.fly.receipts.integration.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aa.fly.receipts.ReceiptsApplication;
import com.aa.fly.receipts.data.ReceiptRepository;
import com.aa.fly.receipts.domain.SearchCriteria;

/**
 * Created by 629874 on 5/11/2019.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReceiptsApplication.class)

public class ReceiptRepositoryTest {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Test
    public void findReceipt() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setTicketNumber("0012362111828");

        assertEquals("251.2000", receiptRepository.findReceipt(criteria).getReceiptTotal());

    }

    // @Test
    // public void findWifiReceipt() throws ParseException {
    // WifiSearchCriteria criteria = new WifiSearchCriteria();
    // criteria.setLastName("smith");
    // criteria.setStartDate(dateFormat.parse("01/01/2017"));
    // criteria.setEndDate(dateFormat.parse("06/01/2019"));
    //
    // assertEquals(1,
    // receiptRepository.findWifiReceipt(criteria).getWifiLineItems().size());
    //
    // }

}
