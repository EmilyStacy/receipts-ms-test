package com.aa.fly.receipts.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusMessageTest {
	
	@Test
	void testGetStatusMessage() throws Exception {
		Assertions.assertEquals("BulkTicket", StatusMessage.BULK_TICKET.getStatusMessage());
		Assertions.assertEquals("AgencyTicket", StatusMessage.AGENCY_TICKET.getStatusMessage());
	}
}
