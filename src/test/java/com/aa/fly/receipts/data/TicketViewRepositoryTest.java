package com.aa.fly.receipts.data;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@RunWith(MockitoJUnitRunner.class)
public class TicketViewRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;
    
    @InjectMocks
    private TicketViewRepository ticketViewRepository;

	@Test
	public void testFindTicketViewByTicketNumberSqlRowSetNull() throws Exception {
		
	    when(jdbcTemplate.queryForRowSet(anyString(), anyString()))
	            .thenReturn(null);
	    
	    SqlRowSet sqlRowSetAssert = ticketViewRepository.findTicketViewByTicketNumber("");
	    assertNull(sqlRowSetAssert);
	}
}
