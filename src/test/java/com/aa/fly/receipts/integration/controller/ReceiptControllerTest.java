package com.aa.fly.receipts.integration.controller;

import java.util.Date;

import com.aa.fly.receipts.controller.ReceiptController;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.service.ReceiptService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;

import com.aa.fly.receipts.domain.SearchCriteria;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by 629874 on 5/7/2019.
 */


@WebMvcTest(value = ReceiptController.class, excludeFilters = {@ComponentScan.Filter(
      type = FilterType.REGEX, pattern = "com.aa.fly.receipts.config.*")} )
@RunWith(SpringRunner.class)

public class ReceiptControllerTest
{

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private ReceiptService receiptService;


   @Test
   public void findReceipt_byTicketNumber() throws Exception
   {


      when(receiptService.findReceipt(any())).thenReturn(getReceipt());
      RequestBuilder request = MockMvcRequestBuilders
            .post("/api/receipt")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(searchCriteriaJson()
            );

      MvcResult mvcResult = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json("{\n" + "   \"firstName\": FirstName,\n"
                  + "   \"lastName\": LastName,\n" + "   \"receiptTotal\": \"239.00\"\n" + "}"))
            .andReturn();

      Assert.assertTrue(mvcResult.getResponse().getContentAsString().contains("239.00"));

   }

   private Receipt getReceipt()
   {
      Receipt receipt = new Receipt();
      receipt.setReceiptTotal("239.00");
      receipt.setLastName("LastName");
      receipt.setFirstName("FirstName");
      return receipt;
   }

   private String searchCriteriaJson()
   {
      return "{ \"ticketNumber\": \"" + "0012362111828" +"\"}";
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
