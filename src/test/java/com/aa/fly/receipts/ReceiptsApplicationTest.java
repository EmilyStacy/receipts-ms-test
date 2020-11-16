package com.aa.fly.receipts;

import com.aa.fly.receipts.config.AppConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
public class ReceiptsApplicationTest {

   @InjectMocks
   private ReceiptsApplication receiptsApplication;
   @Mock
   private AppConfig appConfig;
   @Mock
   private JdbcTemplate jdbcTemplate;
   @Mock
   private DataSource dataSource;
	
   @Test
   public void ensureApplicationStarts()
   {
      Mockito.when(appConfig.jdbcTemplateTicketReceipts(null)).thenReturn(jdbcTemplate);
      Mockito.when(appConfig.jdbcTemplateWifi(null)).thenReturn(jdbcTemplate);
      Mockito.when(appConfig.dataSourceTicketReceipts()).thenReturn(dataSource);
      Mockito.when(appConfig.dataSourceWifi()).thenReturn(dataSource);
      receiptsApplication.main( new String[] {} );
      Assert.assertNotNull( ReceiptsApplication.class );
   }
}

