package com.aa.fly.receipts.manager;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aa.fly.receipts.exception.AgencyTicketException;

class AgencyTicketManagerTest {
	
	private AgencyTicketManager agencyTicketManager = new AgencyTicketManager();

	@BeforeEach
	void setup() {
		List<String> formCodes = new ArrayList<>();
		formCodes.add("21");
		formCodes.add("23");
		formCodes.add("24");
		agencyTicketManager.setNonAgencyFormCodes(formCodes);
	}
	
	@Test
	void testCheckThrowsAgencyTicketException() throws Exception {
		Assertions.assertThrows(AgencyTicketException.class, 
				() -> {
					agencyTicketManager.check("0010000000000");
				});
		Assertions.assertThrows(AgencyTicketException.class, 
				() -> {
					agencyTicketManager.check("0012200000000");
				});
		Assertions.assertThrows(AgencyTicketException.class, 
				() -> {
					agencyTicketManager.check("0012500000000");
				});
	}

	@Test
	void testCheckNotThrowsAgencyTicketException() throws Exception {
		Assertions.assertDoesNotThrow( 
				() -> {
					agencyTicketManager.check("0012100000000");
				});
		Assertions.assertDoesNotThrow( 
				() -> {
					agencyTicketManager.check("0012300000000");
				});
		Assertions.assertDoesNotThrow(
				() -> {
					agencyTicketManager.check("0012400000000");
				});
	}
	
}
