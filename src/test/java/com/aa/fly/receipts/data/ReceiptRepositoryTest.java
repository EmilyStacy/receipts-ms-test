package com.aa.fly.receipts.data;

import com.aa.fly.receipts.ReceiptsApplication;
import com.aa.fly.receipts.domain.SearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by 629874 on 5/11/2019.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReceiptsApplication.class)

public class ReceiptRepositoryTest
{
   @Autowired
   private ReceiptRepository receiptRepository;



   @Test
   public void findReceipt() {
      SearchCriteria criteria = new SearchCriteria();
      criteria.setTicketNumber("0012362111828");

      assertEquals("251.2000", receiptRepository.findReceipt(criteria).getReceiptTotal());

   }
}
