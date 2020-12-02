package com.aa.fly.receipts.data.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

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

@RunWith(SpringJUnit4ClassRunner.class)
public class PnrSegmentBuilderTest {
    @Mock
    private AirportService airportService;
    @InjectMocks
    private PnrSegmentBuilder pnrSegmentBuilder;

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
		assertEquals(Constants.SEG_DEPT_DT, Constants.dateFormat.format(this.ticketReceipt.getSegmentDetails().get(0).getSegmentDepartureDate()));
		assertEquals(Constants.SEG_DEPT_TM, this.ticketReceipt.getSegmentDetails().get(0).getSegmentDepartureTime());
		assertEquals(Constants.SEG_DEPT_ARPRT_CD, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCode());
		assertEquals(Constants.SEG_DEPT_ARPRT_NAME, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getName());
		assertEquals(Constants.SEG_DEPT_ARPRT_CITY, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCity());
		assertEquals(Constants.SEG_DEPT_ARPRT_STATE, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getStateCode());
		assertEquals(Constants.SEG_DEPT_ARPRT_COUNTRY_CD, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCountryCode());
		assertEquals(Constants.SEG_DEPT_ARPRT_COUNTRY_NM, this.ticketReceipt.getSegmentDetails().get(0).getDepartureAirport().getCountryName());
		assertEquals(Constants.SEG_ARVL_DT, Constants.dateFormat.format(this.ticketReceipt.getSegmentDetails().get(0).getSegmentArrivalDate()));
		assertEquals(Constants.SEG_ARVL_TM, this.ticketReceipt.getSegmentDetails().get(0).getSegmentArrivalTime());
		assertEquals(Constants.SEG_ARVL_ARPRT_CD, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCode());
		assertEquals(Constants.SEG_ARVL_ARPRT_NAME, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getName());
		assertEquals(Constants.SEG_ARVL_ARPRT_CITY, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCity());
		assertEquals(Constants.SEG_ARVL_ARPRT_STATE, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getStateCode());
		assertEquals(Constants.SEG_ARVL_ARPRT_COUNTRY_CD, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCountryCode());
		assertEquals(Constants.SEG_ARVL_ARPRT_COUNTRY_NM, this.ticketReceipt.getSegmentDetails().get(0).getArrivalAirport().getCountryName());
		assertEquals(Constants.SEG_COUPON_STATUS_CD, this.ticketReceipt.getSegmentDetails().get(0).getSegmentStatus());
		assertEquals(Constants.SEG_OPERAT_CARRIER_CD, this.ticketReceipt.getSegmentDetails().get(0).getCarrierCode());
		assertEquals(Constants.FLIGHT_NBR, this.ticketReceipt.getSegmentDetails().get(0).getFlightNumber());
		assertEquals(Constants.BOOKING_CLASS, this.ticketReceipt.getSegmentDetails().get(0).getBookingClass());
		assertEquals(Constants.FARE_BASE, this.ticketReceipt.getSegmentDetails().get(0).getFareBasis());
		assertEquals("false", this.ticketReceipt.getSegmentDetails().get(0).getReturnTrip());
	}
	
	private void mockAirports() {
	    Mockito.when(airportService.getAirport(Constants.SEG_DEPT_ARPRT_CD)).thenReturn(getAirport(Constants.SEG_DEPT_ARPRT_CD, Constants.SEG_DEPT_ARPRT_NAME, Constants.SEG_DEPT_ARPRT_CITY, Constants.SEG_DEPT_ARPRT_STATE, Constants.SEG_DEPT_ARPRT_COUNTRY_CD, Constants.SEG_DEPT_ARPRT_COUNTRY_NM));
	    Mockito.when(airportService.getAirport(Constants.SEG_ARVL_ARPRT_CD)).thenReturn(getAirport(Constants.SEG_ARVL_ARPRT_CD, Constants.SEG_ARVL_ARPRT_NAME, Constants.SEG_ARVL_ARPRT_CITY, Constants.SEG_ARVL_ARPRT_STATE, Constants.SEG_ARVL_ARPRT_COUNTRY_CD, Constants.SEG_ARVL_ARPRT_COUNTRY_NM));
	    Mockito.when(airportService.getAirport("")).thenReturn(null);		
	}
		
	private void mockTicketReceiptRsRow() throws ParseException {
		this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
		.segDeptDt(Constants.dateFormat.parse(Constants.SEG_DEPT_DT))
		.segDeptTm(Constants.SEG_DEPT_TM)
		.segDeptArprtCd(Constants.SEG_DEPT_ARPRT_CD)
		.segArvltDt(Constants.dateFormat.parse(Constants.SEG_ARVL_DT))
		.segArvlTm(Constants.SEG_ARVL_TM)
		.segArvlArprtCd(Constants.SEG_ARVL_ARPRT_CD)
		.segCouponStatusCd(Constants.SEG_COUPON_STATUS_CD)
		.segOperatCarrierCd(Constants.SEG_OPERAT_CARRIER_CD)
		.flightNbr(Constants.FLIGHT_NBR)
		.bookingClass(Constants.BOOKING_CLASS)
		.fareBase(Constants.FARE_BASE)
		.couponSeqNbr(Constants.COUPON_SEQ_NBR)
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
