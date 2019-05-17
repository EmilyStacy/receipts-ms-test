package com.aa.fly.receipts.data;

import com.aa.fly.receipts.ReceiptsApplication;
import com.aa.fly.receipts.domain.SearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class ReceiptRepositoryTest
{
   @Mock
   private JdbcTemplate jdbcTemplate;

   @InjectMocks
   private ReceiptRepository receiptRepository;

   @Test
   public void findReceipt() {
      SearchCriteria criteria = new SearchCriteria();
      criteria.setTicketNumber("0012362111828");
      when(jdbcTemplate.queryForObject(anyString(), any(), eq(String.class) )).thenReturn("251.2000");
      assertEquals("251.2000", receiptRepository.findReceipt(criteria).getReceiptTotal());

   }
}
