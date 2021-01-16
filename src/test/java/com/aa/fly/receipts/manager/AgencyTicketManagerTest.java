package com.aa.fly.receipts.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.aa.fly.receipts.data.TicketViewRepository;
import com.aa.fly.receipts.exception.AgencyTicketException;

@ExtendWith(MockitoExtension.class)
class AgencyTicketManagerTest {
    @Mock
    private TicketViewRepository ticketViewRepository;
    
    @Mock
    private SqlRowSet sqlRowSet;
    
    @InjectMocks
    private AgencyTicketManager agencyTicketManager;
	
	@Test
	void testCheckThrowsAgencyTicketException() throws Exception {
		
		Mockito.when(ticketViewRepository.findTicketViewByTicketNumber(Mockito.anyString()))
        .thenReturn(sqlRowSet);
		
		Mockito.when(sqlRowSet.next())
        .thenReturn(true);
		
		Assertions.assertThrows(AgencyTicketException.class, 
				() -> {
					agencyTicketManager.check("");
				});
	}

	@Test
	void testCheckNotThrowsAgencyTicketException() throws Exception {
		
		Mockito.when(ticketViewRepository.findTicketViewByTicketNumber(Mockito.anyString()))
        .thenReturn(sqlRowSet);
		
		Mockito.when(sqlRowSet.next())
        .thenReturn(false);

		Assertions.assertDoesNotThrow(
				() -> {
					agencyTicketManager.check("");
				});
	}
}
