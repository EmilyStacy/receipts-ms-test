package com.aa.fly.receipts.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
	
    @BeforeEach void setup() {
		agencyTicketManager.setHistory("18");    	
    }
    
	@Test
	void testCheckTicketFoundThrowsAgencyTicketException() throws Exception {
		
		Mockito.when(ticketViewRepository.findTicketViewByTicketNumber(Mockito.anyString()))
        .thenReturn(sqlRowSet);
		
		Mockito.when(sqlRowSet.next())
        .thenReturn(true);
		
		Mockito.when(sqlRowSet.getString("TICKET_ISSUE_DT"))
        .thenReturn("2021-1-1");
		
		Assertions.assertThrows(AgencyTicketException.class,
				() -> {
					agencyTicketManager.check("");
				});
	}
    
	@Test
	void testCheckTicketFoundNotThrowsAgencyTicketExceptionBeyondHistory() throws Exception {
		
		Mockito.when(ticketViewRepository.findTicketViewByTicketNumber(Mockito.anyString()))
        .thenReturn(sqlRowSet);
		
		Mockito.when(sqlRowSet.next())
        .thenReturn(true);
		
		Mockito.when(sqlRowSet.getString("TICKET_ISSUE_DT"))
        .thenReturn("2010-1-1");
		
		Assertions.assertDoesNotThrow(
				() -> {
					agencyTicketManager.check("");
				});
	}

	@Test
	void testCheckTicketNotFoundNotThrowsAgencyTicketException() throws Exception {
		
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
