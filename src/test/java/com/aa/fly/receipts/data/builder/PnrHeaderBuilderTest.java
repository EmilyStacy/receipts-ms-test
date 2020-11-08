package com.aa.fly.receipts.data.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.AirportService;

@RunWith(SpringJUnit4ClassRunner.class)
public class PnrHeaderBuilderTest {
    @Mock
    private AirportService airportService;
    @InjectMocks
    private PnrHeaderBuilder pnrHeaderBuilder;

	static final String AIRLN_ACCT_CD = "001";
	static final String TICKET_ISSUE_DT = "2020-10-10";
	static final String DEP_DT = "2020-12-10";
	static final String PNR = "ABCDEF";
	static final String ORG_ATO_CD = "MCO";
	static final String ORG_ATO_NAME = "Orlando International";
	static final String ORG_ATO_CITY = "Orlando";
	static final String ORG_ATO_STATE = "FL";
	static final String ORG_ATO_COUNTRY_CD = "USA";
	static final String ORG_ATO_COUNTRY_NM = "United States";
	static final String DEST_ATO_CD = "MIA";
	static final String DEST_ATO_NAME = "Miami International";
	static final String DEST_ATO_CITY = "Miami";
	static final String DEST_ATO_STATE = "FL";
	static final String DEST_ATO_COUNTRY_CD = "USA";
	static final String DEST_ATO_COUNTRY_NM = "United States";
	
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private TicketReceipt ticketReceipt = new TicketReceipt();
    private TicketReceiptRsRow ticketReceiptRsRow = null;
    
	@Test
	public void testBuild_Pnr_Header_Empty_Airport_Code() throws Exception {
		this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
		.orgAtoCd("")
		.destAtoCd("")
		.build();
		this.mockAirports();

		pnrHeaderBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);
		
		assertNotNull(this.ticketReceipt);
		assertEquals(null, this.ticketReceipt.getOriginAirport());
		assertEquals(null, this.ticketReceipt.getDestinationAirport());
	}
	
	@Test
	public void testBuild_Pnr_Header() throws Exception {
		this.mockTicketReceiptRsRow();
		this.mockAirports();

		pnrHeaderBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);
		
		assertNotNull(this.ticketReceipt);
		assertEquals(AIRLN_ACCT_CD, this.ticketReceipt.getAirlineAccountCode());
		assertEquals(TICKET_ISSUE_DT, dateFormat.format(this.ticketReceipt.getTicketIssueDate()));
		assertEquals(DEP_DT, dateFormat.format(this.ticketReceipt.getDepartureDate()));
		assertEquals(PNR, this.ticketReceipt.getPnr());
		assertEquals(ORG_ATO_CD, this.ticketReceipt.getOriginAirport().getCode());
		assertEquals(ORG_ATO_NAME, this.ticketReceipt.getOriginAirport().getName());
		assertEquals(ORG_ATO_CITY, this.ticketReceipt.getOriginAirport().getCity());
		assertEquals(ORG_ATO_STATE, this.ticketReceipt.getOriginAirport().getStateCode());
		assertEquals(ORG_ATO_COUNTRY_CD, this.ticketReceipt.getOriginAirport().getCountryCode());
		assertEquals(ORG_ATO_COUNTRY_NM, this.ticketReceipt.getOriginAirport().getCountryName());
		assertEquals(DEST_ATO_CD, this.ticketReceipt.getDestinationAirport().getCode());
		assertEquals(DEST_ATO_NAME, this.ticketReceipt.getDestinationAirport().getName());
		assertEquals(DEST_ATO_CITY, this.ticketReceipt.getDestinationAirport().getCity());
		assertEquals(DEST_ATO_STATE, this.ticketReceipt.getDestinationAirport().getStateCode());
		assertEquals(DEST_ATO_COUNTRY_CD, this.ticketReceipt.getDestinationAirport().getCountryCode());
		assertEquals(DEST_ATO_COUNTRY_NM, this.ticketReceipt.getDestinationAirport().getCountryName());
	}
	
	private void mockAirports() {
	    Mockito.when(airportService.getAirport(ORG_ATO_CD)).thenReturn(getAirport(ORG_ATO_CD, ORG_ATO_NAME, ORG_ATO_CITY, ORG_ATO_STATE, ORG_ATO_COUNTRY_CD, ORG_ATO_COUNTRY_NM));
	    Mockito.when(airportService.getAirport(DEST_ATO_CD)).thenReturn(getAirport(DEST_ATO_CD, DEST_ATO_NAME, DEST_ATO_CITY, DEST_ATO_STATE, DEST_ATO_COUNTRY_CD, DEST_ATO_COUNTRY_NM));		
	    Mockito.when(airportService.getAirport("")).thenReturn(null);		
	}
	
	private void mockTicketReceiptRsRow() throws ParseException {
		this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
		.airlnAcctCd(AIRLN_ACCT_CD)
		.ticketIssueDt(dateFormat.parse(TICKET_ISSUE_DT))
		.depDt(dateFormat.parse(DEP_DT))
		.pnr(PNR)
		.orgAtoCd(ORG_ATO_CD)
		.destAtoCd(DEST_ATO_CD)
		.build();
	}
	
	private Airport getAirport(String code, String name, String city, String state, String countryCode, String countryName) {
        Airport airport = new Airport();
        airport.setCode(code);
        airport.setName(name);
        airport.setCity(city);
        airport.setStateCode(state);
        airport.setCountryCode(countryCode);
        airport.setCountryName(countryName);
        
        return airport;
    }
}
