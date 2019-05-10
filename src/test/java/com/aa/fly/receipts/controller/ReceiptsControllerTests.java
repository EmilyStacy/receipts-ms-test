package com.aa.fly.receipts.controller;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.controller.ReceiptsController;
import com.aa.fly.receipts.domain.ReceiptResponse;
import com.aa.fly.receipts.domain.SearchCriteria;

import com.aa.fly.receipts.controller.ReceiptsController;
/**
 * Created by 629874 on 5/7/2019.
 */

@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest( classes = ReceiptsController.class )
@DirtiesContext
public class ReceiptsControllerTests
{
   @Autowired
   private ReceiptsController receiptsController;

   @Test
   public void testSearchReceipts() {
      SearchCriteria searchCriteria = getSearchCriteria();
      ReceiptResponse response = receiptsController.getReceipts(searchCriteria);
      Assert.assertEquals(searchCriteria.getFirstName(), response.getFirstName());
      Assert.assertEquals(searchCriteria.getLastName(), response.getLastName());
      Assert.assertEquals("$239.00", response.getReceiptTotal());

   }

   private SearchCriteria getSearchCriteria() {
      SearchCriteria searchCriteria = new SearchCriteria();
      searchCriteria.setDepartureDate(new Date());
      searchCriteria.setFirstName("first");
      searchCriteria.setLastName("last");
      searchCriteria.setTicketNumber("1234");
      return searchCriteria;
   }
}
