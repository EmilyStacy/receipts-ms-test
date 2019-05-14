package com.aa.fly.receipts.service;

import com.aa.fly.receipts.data.ReceiptRepository;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.service.impl.ReceiptServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by 629874 on 5/9/2019.
 */

@RunWith(MockitoJUnitRunner.class)
public class ReceiptServiceTest
{
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

   private Receipt getReceipt()
   {
      Receipt receipt = new Receipt();
      receipt.setReceiptTotal("239.00");
      receipt.setLastName("LastName");
      receipt.setFirstName("FirstName");
      return receipt;
   }

}
