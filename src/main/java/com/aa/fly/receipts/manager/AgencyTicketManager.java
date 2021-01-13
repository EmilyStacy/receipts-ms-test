package com.aa.fly.receipts.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.data.TicketViewRepository;
import com.aa.fly.receipts.exception.AgencyTicketException;
import com.aa.fly.receipts.exception.StatusMessage;

@Component
public class AgencyTicketManager {
	
	@Autowired
	TicketViewRepository ticketViewRepository;

	public void check(String ticketNumber) {

		SqlRowSet sqlRowSet = ticketViewRepository.findTicketViewByTicketNumber(ticketNumber);
		
        if (sqlRowSet.next()) {
	        throw new AgencyTicketException(StatusMessage.AGENCY_TICKET.getStatusMessage());			
        }
	}
}
