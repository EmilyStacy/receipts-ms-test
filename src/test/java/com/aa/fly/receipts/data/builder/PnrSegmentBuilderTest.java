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
public class PnrSegmentBuilderTest {
    @Mock
    private AirportService airportService;
    @InjectMocks
    private PnrSegmentBuilder pnrSegmentBuilder;

	static final String SEG_DEPT_DT = "2020-10-10";
	static final String SEG_DEPT_TM = "08:50:00";
	static final String SEG_DEPT_ARPRT_CD = "MCO";
	static final String SEG_DEPT_ARPRT_NAME = "Orlando International";
	static final String SEG_DEPT_ARPRT_CITY = "Orlando";
	static final String SEG_DEPT_ARPRT_STATE = "FL";
	static final String SEG_DEPT_ARPRT_COUNTRY_CD = "USA";
	static final String SEG_DEPT_ARPRT_COUNTRY_NM = "United States";
	static final String SEG_ARVL_DT = "2020-10-11";
	static final String SEG_ARVL_TM = "09:50:00";
	static final String SEG_ARVL_ARPRT_CD = "MIA";
	static final String SEG_ARVL_ARPRT_NAME = "Miami International";
	static final String SEG_ARVL_ARPRT_CITY = "Miami";
	static final String SEG_ARVL_ARPRT_STATE = "FL";
	static final String SEG_ARVL_ARPRT_COUNTRY_CD = "USA";
	static final String SEG_ARVL_ARPRT_COUNTRY_NM = "United States";	
	static final String SEG_COUPON_STATUS_CD = "OK";
	static final String SEG_OPERAT_CARRIER_CD = "AA";
	static final String FLIGHT_NBR = "1112";
	static final String BOOKING_CLASS = "B";
	static final String FARE_BASE = "OVBZZNB5";
	static final String COUPON_SEQ_NBR = "1";
	
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private TicketReceipt ticketReceipt = new TicketReceipt();
    private TicketReceiptRsRow ticketReceiptRsRow = null;
	
	@Test
	public void testBuild_Pnr_Segment_Not_First_Row_CouponSeqNbr_One_ReturnTrip_True() throws Exception {
		this.mockTicketReceiptRsRow();
		this.mockAirports();
		this.ticketReceiptRsRow.setCouponSeqNbr("1");

		this.ticketReceipt = pnrSegmentBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow, 2);
		
		assertNotNull(this.ticketReceipt);
		assertNotNull(this.ticketReceipt.getSegmentDetails());
		assertEquals(1, this.ticketReceipt.getSegmentDetails().size());
		assertEquals("true", this.ticketReceipt.getSegmentDetails().get(0).getReturnTrip());
	}
	
	@Test
	public void testBuild_Pnr_Segment_First_Row_CouponSeqNbr_One_ReturnTrip_False() throws Exception {
		this.mockTicketReceiptRsRow();
		this.mockAirports();
		this.ticketReceiptRsRow.setCouponSeqNbr("1");

		this.ticketReceipt = pnrSegmentBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow, 0);
		
		assertNotNull(this.ticketReceipt);
		assertNotNull(this.ticketReceipt.getSegmentDetails());
		assertEquals(1, this.ticketReceipt.getSegmentDetails().size());
		assertEquals("false", this.ticketReceipt.getSegmentDetails().get(0).getReturnTrip());
	}
	
	@Test
	public void testBuild_Pnr_Segment_Not_First_Row_CouponSeqNbr_Not_One_ReturnTrip_False() throws Exception {
		this.mockTicketReceiptRsRow();
		this.mockAirports();
		this.ticketReceiptRsRow.setCouponSeqNbr("2");

		this.ticketReceipt = pnrSegmentBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow, 2);
		
		assertNotNull(this.ticketReceipt);
		assertNotNull(this.ticketReceipt.getSegmentDetails());
		assertEquals(1, this.ticketReceipt.getSegmentDetails().size());
		assertEquals("false", this.ticketReceipt.getSegmentDetails().get(0).getReturnTrip());
	}
	
	@Test
	public void testBuild_Pnr_Segment() throws Exception {
		this.mockTicketReceiptRsRow();
		this.mockAirports();
		
		this.ticketReceipt = pnrSegmentBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow, 0);
		
		assertNotNull(this.ticketReceipt);
		assertNotNull(this.ticketReceipt.getSegmentDetails());
		assertEquals(1, this.ticketReceipt.getSegmentDetails().size());
		assertEquals(SEG_DEPT_DT, dateFormat.format(this.ticketReceipt.getSegmentDetails().get(0).getSegmentDepartureDate()));
		assertEquals(SEG_DEPT_TM, this.ticketReceipt.getSegmentDetails().get(0).getSegmentDepartureTime());
		assertEquals(SEG_DEPT_ARPRT_CD, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCode());
		assertEquals(SEG_DEPT_ARPRT_NAME, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getName());
		assertEquals(SEG_DEPT_ARPRT_CITY, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCity());
		assertEquals(SEG_DEPT_ARPRT_STATE, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getStateCode());
		assertEquals(SEG_DEPT_ARPRT_COUNTRY_CD, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCountryCode());
		assertEquals(SEG_DEPT_ARPRT_COUNTRY_NM, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCountryName());
		assertEquals(SEG_ARVL_DT, dateFormat.format(this.ticketReceipt.getSegmentDetails().get(0).getSegmentArrivalDate()));
		assertEquals(SEG_ARVL_TM, this.ticketReceipt.getSegmentDetails().get(0).getSegmentArrivalTime());
		assertEquals(SEG_ARVL_ARPRT_CD, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCode());
		assertEquals(SEG_ARVL_ARPRT_NAME, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getName());
		assertEquals(SEG_ARVL_ARPRT_CITY, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCity());
		assertEquals(SEG_ARVL_ARPRT_STATE, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getStateCode());
		assertEquals(SEG_ARVL_ARPRT_COUNTRY_CD, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCountryCode());
		assertEquals(SEG_ARVL_ARPRT_COUNTRY_NM, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCountryName());
		assertEquals(SEG_COUPON_STATUS_CD, this.ticketReceipt.getSegmentDetails().get(0).getSegmentStatus());
		assertEquals(SEG_OPERAT_CARRIER_CD, this.ticketReceipt.getSegmentDetails().get(0).getCarrierCode());
		assertEquals(FLIGHT_NBR, this.ticketReceipt.getSegmentDetails().get(0).getFlightNumber());
		assertEquals(BOOKING_CLASS, this.ticketReceipt.getSegmentDetails().get(0).getBookingClass());
		assertEquals(FARE_BASE, this.ticketReceipt.getSegmentDetails().get(0).getFareBasis());
		assertEquals("false", this.ticketReceipt.getSegmentDetails().get(0).getReturnTrip());
	}
	
	private void mockAirports() {
	    Mockito.when(airportService.getAirport(SEG_DEPT_ARPRT_CD)).thenReturn(getAirport(SEG_DEPT_ARPRT_CD, SEG_DEPT_ARPRT_NAME, SEG_DEPT_ARPRT_CITY, SEG_DEPT_ARPRT_STATE, SEG_DEPT_ARPRT_COUNTRY_CD, SEG_DEPT_ARPRT_COUNTRY_NM));
	    Mockito.when(airportService.getAirport(SEG_ARVL_ARPRT_CD)).thenReturn(getAirport(SEG_ARVL_ARPRT_CD, SEG_ARVL_ARPRT_NAME, SEG_ARVL_ARPRT_CITY, SEG_ARVL_ARPRT_STATE, SEG_ARVL_ARPRT_COUNTRY_CD, SEG_ARVL_ARPRT_COUNTRY_NM));
	    Mockito.when(airportService.getAirport("")).thenReturn(null);		
	}
		
	private void mockTicketReceiptRsRow() throws ParseException {
		this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
		.segDeptDt(dateFormat.parse(SEG_DEPT_DT))
		.segDeptTm(SEG_DEPT_TM)
		.segDeptArprtCd(SEG_DEPT_ARPRT_CD)
		.segArvltDt(dateFormat.parse(SEG_ARVL_DT))
		.segArvlTm(SEG_ARVL_TM)
		.segArvlArprtCd(SEG_ARVL_ARPRT_CD)
		.segCouponStatusCd(SEG_COUPON_STATUS_CD)
		.segOperatCarrierCd(SEG_OPERAT_CARRIER_CD)
		.flightNbr(FLIGHT_NBR)
		.bookingClass(BOOKING_CLASS)
		.fareBase(FARE_BASE)
		.couponSeqNbr(COUPON_SEQ_NBR)
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
