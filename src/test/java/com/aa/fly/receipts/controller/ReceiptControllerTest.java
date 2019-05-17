package com.aa.fly.receipts.controller;

import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.service.ReceiptService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by 629874 on 5/17/2019.
 */


@RunWith(SpringJUnit4ClassRunner.class)

public class ReceiptControllerTest
{
   @Mock
   private ReceiptService receiptService;

   @InjectMocks
   private ReceiptController controller;


   @Test
   public void findReceipt_byTicketNumber() throws Exception
   {
      final SearchCriteria searchCriteria = getSearchCriteria();
      final Receipt receiptExpected = getReceipt(searchCriteria);

      when(receiptService.findReceipt(any())).thenReturn(receiptExpected);

      Receipt receiptAcutal =  controller.getReceipts(searchCriteria);

      assertThat(receiptAcutal.getFirstName()).isEqualToIgnoringCase(searchCriteria.getFirstName());
      assertThat(receiptAcutal.getLastName()).isEqualToIgnoringCase(searchCriteria.getLastName());
      assertThat(receiptAcutal.getReceiptTotal()).isEqualToIgnoringCase(receiptExpected.getReceiptTotal());

   }

   public static Receipt getReceipt(SearchCriteria searchCriteria)
   {
      Receipt receipt = new Receipt();
      receipt.setReceiptTotal("239.00");
      receipt.setLastName(searchCriteria.getLastName());
      receipt.setFirstName(searchCriteria.getFirstName());
      return receipt;
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
