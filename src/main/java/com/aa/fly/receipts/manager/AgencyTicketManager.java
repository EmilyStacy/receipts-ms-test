package com.aa.fly.receipts.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.data.TicketViewRepository;
import com.aa.fly.receipts.exception.AgencyTicketException;
import com.aa.fly.receipts.exception.StatusMessage;

@Component
public class AgencyTicketManager {
    private final DateFormat formatMMddyyyy = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${mosaic.data.history.month}")
	private String history;
	
	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	@Autowired
	TicketViewRepository ticketViewRepository;

	public void check(String ticketNumber) throws ParseException {

		SqlRowSet sqlRowSet = ticketViewRepository.findTicketViewByTicketNumber(ticketNumber);
		
        if (sqlRowSet.next() && !isBeyondHistory(sqlRowSet.getString("TICKET_ISSUE_DT"))) {
    	        throw new AgencyTicketException(StatusMessage.AGENCY_TICKET.getStatusMessage());	        		
        }
	}
	
	// Was ticket issued beyond supported history?
	private boolean isBeyondHistory(String issueDate) throws ParseException {
		
		Date dateIssueDate = formatMMddyyyy.parse(issueDate);

		Calendar calToday = Calendar.getInstance();
		Calendar calIssueDate = Calendar.getInstance();
		calIssueDate.setTime(dateIssueDate);
		calIssueDate.add(Calendar.MONTH, Integer.parseInt(history));
		
		return calIssueDate.before(calToday);
	}
}
