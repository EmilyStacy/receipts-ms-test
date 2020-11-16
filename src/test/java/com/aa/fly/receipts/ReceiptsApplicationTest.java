package com.aa.fly.receipts;

import com.aa.fly.receipts.config.AppConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
public class ReceiptsApplicationTest {

   @Mock
   private AppConfig appConfig;
   @Mock
   private JdbcTemplate jdbcTemplate;
   @Mock
   private DataSource dataSource;

   @Before
   public void setUp() {
      MockitoAnnotations.initMocks(this);
   }
	
   @Test
   public void ensureApplicationStarts()
   {
      Mockito.when(appConfig.jdbcTemplateTicketReceipts(null)).thenReturn(jdbcTemplate);
      Mockito.when(appConfig.jdbcTemplateWifi(null)).thenReturn(jdbcTemplate);
      Mockito.when(appConfig.dataSourceTicketReceipts()).thenReturn(dataSource);
      Mockito.when(appConfig.dataSourceWifi()).thenReturn(dataSource);


      ReceiptsApplication.main( new String[] {} );
      Assert.assertNotNull( ReceiptsApplication.class );
   }
}

