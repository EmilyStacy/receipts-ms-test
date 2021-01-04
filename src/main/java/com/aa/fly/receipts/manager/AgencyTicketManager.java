package com.aa.fly.receipts.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.exception.AgencyTicketException;
import com.aa.fly.receipts.exception.StatusMessage;

@Component
public class AgencyTicketManager {
	
	@Value("${nonAgencyFormCodes}")
	private List<String> nonAgencyFormCodes;
	
	public void setNonAgencyFormCodes(List<String> nonAgencyFormCodes) {
		this.nonAgencyFormCodes = nonAgencyFormCodes;
	}

	public List<String> getNonAgencyFormCodes() {
		return nonAgencyFormCodes;
	}

	public void check(String ticketNumber) {
		String formCode = ticketNumber.substring(3).substring(0, 2);
		
		if (!nonAgencyFormCodes.contains(formCode)) {
	        throw new AgencyTicketException(StatusMessage.AGENCY_TICKET.getStatusMessage());			
		}
	}
}
