package com.aa.fly.receipts.data.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;

@RunWith(SpringJUnit4ClassRunner.class)
public class PnrHeaderBuilderTest {
    @Mock
    private AirportService airportService;
    @InjectMocks
    private PnrHeaderBuilder pnrHeaderBuilder;

    private TicketReceipt ticketReceipt = new TicketReceipt();
    private TicketReceiptRsRow ticketReceiptRsRow = null;
    
	@Test
	public void testBuild_Pnr_Header_Empty_Airport_Code() throws Exception {
		this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
		.orgAtoCd("")
		.destAtoCd("")
		.build();
		this.mockAirports();

		this.ticketReceipt = pnrHeaderBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);
		
		assertNotNull(this.ticketReceipt);
		assertEquals(null, this.ticketReceipt.getOriginAirport());
		assertEquals(null, this.ticketReceipt.getDestinationAirport());
	}
	
	@Test
	public void testBuild_Pnr_Header() throws Exception {
		this.ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		this.mockAirports();

		pnrHeaderBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);
		
		assertNotNull(this.ticketReceipt);
		assertEquals(Constants.AIRLN_ACCT_CD, this.ticketReceipt.getAirlineAccountCode());
		assertEquals(Constants.TICKET_ISSUE_DT, Constants.dateFormat.format(this.ticketReceipt.getTicketIssueDate()));
		assertEquals(Constants.DEP_DT, Constants.dateFormat.format(this.ticketReceipt.getDepartureDate()));
		assertEquals(Constants.PNR, this.ticketReceipt.getPnr());
		assertEquals(Constants.ORG_ATO_CD, this.ticketReceipt.getOriginAirport().getCode());
		assertEquals(Constants.ORG_ATO_NAME, this.ticketReceipt.getOriginAirport().getName());
		assertEquals(Constants.ORG_ATO_CITY, this.ticketReceipt.getOriginAirport().getCity());
		assertEquals(Constants.ORG_ATO_STATE, this.ticketReceipt.getOriginAirport().getStateCode());
		assertEquals(Constants.ORG_ATO_COUNTRY_CD, this.ticketReceipt.getOriginAirport().getCountryCode());
		assertEquals(Constants.ORG_ATO_COUNTRY_NM, this.ticketReceipt.getOriginAirport().getCountryName());
		assertEquals(Constants.DEST_ATO_CD, this.ticketReceipt.getDestinationAirport().getCode());
		assertEquals(Constants.DEST_ATO_NAME, this.ticketReceipt.getDestinationAirport().getName());
		assertEquals(Constants.DEST_ATO_CITY, this.ticketReceipt.getDestinationAirport().getCity());
		assertEquals(Constants.DEST_ATO_STATE, this.ticketReceipt.getDestinationAirport().getStateCode());
		assertEquals(Constants.DEST_ATO_COUNTRY_CD, this.ticketReceipt.getDestinationAirport().getCountryCode());
		assertEquals(Constants.DEST_ATO_COUNTRY_NM, this.ticketReceipt.getDestinationAirport().getCountryName());
	}
	
	private void mockAirports() {
	    Mockito.when(airportService.getAirport(Constants.ORG_ATO_CD)).thenReturn(getAirport(Constants.ORG_ATO_CD, Constants.ORG_ATO_NAME, Constants.ORG_ATO_CITY, Constants.ORG_ATO_STATE, Constants.ORG_ATO_COUNTRY_CD, Constants.ORG_ATO_COUNTRY_NM));
	    Mockito.when(airportService.getAirport(Constants.DEST_ATO_CD)).thenReturn(getAirport(Constants.DEST_ATO_CD, Constants.DEST_ATO_NAME, Constants.DEST_ATO_CITY, Constants.DEST_ATO_STATE, Constants.DEST_ATO_COUNTRY_CD, Constants.DEST_ATO_COUNTRY_NM));		
	    Mockito.when(airportService.getAirport("")).thenReturn(null);		
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
