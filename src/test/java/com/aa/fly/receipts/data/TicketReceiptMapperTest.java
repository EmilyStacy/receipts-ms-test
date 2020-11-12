package com.aa.fly.receipts.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.data.builder.PassengerBuilder;
import com.aa.fly.receipts.data.builder.PnrHeaderBuilder;
import com.aa.fly.receipts.data.builder.PnrSegmentBuilder;
import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.AirportService;
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketReceiptMapperTest {
    @Mock
    private SqlRowSet resultSet;

    @Mock
    private AirportService airportService;
    
    @Mock
    private PnrHeaderBuilder pnrHeaderBuilder;
    
    @Mock
    private PassengerBuilder passengerBuilder;

    @Mock
    private PnrSegmentBuilder pnrSegmentBuilder;

    @InjectMocks
    private TicketReceiptMapper ticketReceiptMapper;

    private TicketReceipt ticketReceiptMock, ticketReceiptReturn;
    private TicketReceiptRsRow ticketReceiptRsRow;
    private List<TicketReceiptRsRow> ticketReceiptRsRowList = new ArrayList<>();
    
    private static final String SEG_DEPT_DT2 = "2020-10-11";
    private static final String SEG_DEPT_TM2 = "06:50:00";
    
    @Test
    public void testMapTicketReceipt_Resultset_One_Row() throws ParseException {
    	this.mockTicketReceipt();
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();        
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    	
    	assertNotNull(ticketReceiptReturn);
    	assertEquals(1, ticketReceiptReturn.getPassengerDetails().size());
    	assertEquals(1, ticketReceiptReturn.getSegmentDetails().size());
    	assertEquals(ticketReceiptRsRow.getAirlnAcctCd(), ticketReceiptReturn.getAirlineAccountCode());
    	assertEquals(ticketReceiptRsRow.getTicketIssueDt(), ticketReceiptReturn.getTicketIssueDate());
    	assertEquals(ticketReceiptRsRow.getDepDt(), ticketReceiptReturn.getDepartureDate());
    	assertEquals(ticketReceiptRsRow.getPnr(), ticketReceiptReturn.getPnr());
    	assertEquals(ticketReceiptRsRow.getOrgAtoCd(), ticketReceiptReturn.getOriginAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getDestAtoCd(), ticketReceiptReturn.getDestinationAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getTicketNbr(), ticketReceiptReturn.getPassengerDetails().get(0).getTicketNumber());
    	assertEquals(ticketReceiptRsRow.getFirstNm(), ticketReceiptReturn.getPassengerDetails().get(0).getFirstName());
    	assertEquals(ticketReceiptRsRow.getLastNm(), ticketReceiptReturn.getPassengerDetails().get(0).getLastName());
    	assertEquals(ticketReceiptRsRow.getAadvantNbr(), ticketReceiptReturn.getPassengerDetails().get(0).getAdvantageNumber());
    	assertEquals(ticketReceiptRsRow.getLyltyOwnCd(), ticketReceiptReturn.getPassengerDetails().get(0).getLoyaltyOwnerCode());
    	
    	assertEquals(ticketReceiptRsRow.getSegDeptDt(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentDepartureDate());
    	assertEquals(ticketReceiptRsRow.getSegDeptTm(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentDepartureTime());
    	assertEquals(ticketReceiptRsRow.getSegDeptArprtCd(), ticketReceiptReturn.getSegmentDetails().get(0).getDepartureAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getSegArvltDt(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentArrivalDate());
    	assertEquals(ticketReceiptRsRow.getSegArvlTm(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentArrivalTime());
    	assertEquals(ticketReceiptRsRow.getSegArvlArprtCd(), ticketReceiptReturn.getSegmentDetails().get(0).getArrivalAirport().getCode());

    	assertEquals(ticketReceiptRsRow.getSegCouponStatusCd(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentStatus());
    	assertEquals(ticketReceiptRsRow.getSegOperatCarrierCd(), ticketReceiptReturn.getSegmentDetails().get(0).getCarrierCode());
    	assertEquals(ticketReceiptRsRow.getFlightNbr(), ticketReceiptReturn.getSegmentDetails().get(0).getFlightNumber());
    	assertEquals(ticketReceiptRsRow.getBookingClass(), ticketReceiptReturn.getSegmentDetails().get(0).getBookingClass());
    	assertEquals(ticketReceiptRsRow.getFareBase(), ticketReceiptReturn.getSegmentDetails().get(0).getFareBasis());
    	assertEquals("false", ticketReceiptReturn.getSegmentDetails().get(0).getReturnTrip());
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_Two_Rows_Same_Segment() throws ParseException {
    	this.mockTicketReceipt();
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();        
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    	
    	assertNotNull(ticketReceiptReturn);
    	assertEquals(1, ticketReceiptReturn.getPassengerDetails().size());
    	assertEquals(1, ticketReceiptReturn.getSegmentDetails().size());
    	assertEquals(ticketReceiptRsRow.getAirlnAcctCd(), ticketReceiptReturn.getAirlineAccountCode());
    	assertEquals(ticketReceiptRsRow.getTicketIssueDt(), ticketReceiptReturn.getTicketIssueDate());
    	assertEquals(ticketReceiptRsRow.getDepDt(), ticketReceiptReturn.getDepartureDate());
    	assertEquals(ticketReceiptRsRow.getPnr(), ticketReceiptReturn.getPnr());
    	assertEquals(ticketReceiptRsRow.getOrgAtoCd(), ticketReceiptReturn.getOriginAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getDestAtoCd(), ticketReceiptReturn.getDestinationAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getTicketNbr(), ticketReceiptReturn.getPassengerDetails().get(0).getTicketNumber());
    	assertEquals(ticketReceiptRsRow.getFirstNm(), ticketReceiptReturn.getPassengerDetails().get(0).getFirstName());
    	assertEquals(ticketReceiptRsRow.getLastNm(), ticketReceiptReturn.getPassengerDetails().get(0).getLastName());
    	assertEquals(ticketReceiptRsRow.getAadvantNbr(), ticketReceiptReturn.getPassengerDetails().get(0).getAdvantageNumber());
    	assertEquals(ticketReceiptRsRow.getLyltyOwnCd(), ticketReceiptReturn.getPassengerDetails().get(0).getLoyaltyOwnerCode());

    	assertEquals(ticketReceiptRsRow.getSegDeptDt(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentDepartureDate());
    	assertEquals(ticketReceiptRsRow.getSegDeptTm(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentDepartureTime());
    	assertEquals(ticketReceiptRsRow.getSegDeptArprtCd(), ticketReceiptReturn.getSegmentDetails().get(0).getDepartureAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getSegArvltDt(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentArrivalDate());
    	assertEquals(ticketReceiptRsRow.getSegArvlTm(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentArrivalTime());
    	assertEquals(ticketReceiptRsRow.getSegArvlArprtCd(), ticketReceiptReturn.getSegmentDetails().get(0).getArrivalAirport().getCode());

    	assertEquals(ticketReceiptRsRow.getSegCouponStatusCd(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentStatus());
    	assertEquals(ticketReceiptRsRow.getSegOperatCarrierCd(), ticketReceiptReturn.getSegmentDetails().get(0).getCarrierCode());
    	assertEquals(ticketReceiptRsRow.getFlightNbr(), ticketReceiptReturn.getSegmentDetails().get(0).getFlightNumber());
    	assertEquals(ticketReceiptRsRow.getBookingClass(), ticketReceiptReturn.getSegmentDetails().get(0).getBookingClass());
    	assertEquals(ticketReceiptRsRow.getFareBase(), ticketReceiptReturn.getSegmentDetails().get(0).getFareBasis());
    	assertEquals("false", ticketReceiptReturn.getSegmentDetails().get(0).getReturnTrip());
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_Three_Rows_Two_Segments() throws ParseException {
    	this.mockTicketReceipt();
        this.addOneSegment();
        ticketReceiptMock.getSegmentDetails().get(1).setSegmentDepartureDate(Constants.dateFormat.parse(SEG_DEPT_DT2));
        ticketReceiptMock.getSegmentDetails().get(1).setSegmentDepartureTime(SEG_DEPT_TM2);

    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();    
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);
    	
    	TicketReceiptRsRow ticketReceiptRsRow3 = Utils.mockTicketReceiptRsRow();

    	ticketReceiptRsRow3.setSegDeptDt(Constants.dateFormat.parse(SEG_DEPT_DT2));
    	ticketReceiptRsRow3.setSegDeptTm(SEG_DEPT_TM2);
    	ticketReceiptRsRowList.add(ticketReceiptRsRow3);

    	ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    	
    	assertNotNull(ticketReceiptReturn);
    	assertEquals(1, ticketReceiptReturn.getPassengerDetails().size());
    	assertEquals(2, ticketReceiptReturn.getSegmentDetails().size());
    	assertEquals(ticketReceiptRsRow.getAirlnAcctCd(), ticketReceiptReturn.getAirlineAccountCode());
    	assertEquals(ticketReceiptRsRow.getTicketIssueDt(), ticketReceiptReturn.getTicketIssueDate());
    	assertEquals(ticketReceiptRsRow.getDepDt(), ticketReceiptReturn.getDepartureDate());
    	assertEquals(ticketReceiptRsRow.getPnr(), ticketReceiptReturn.getPnr());
    	assertEquals(ticketReceiptRsRow.getOrgAtoCd(), ticketReceiptReturn.getOriginAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getDestAtoCd(), ticketReceiptReturn.getDestinationAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getTicketNbr(), ticketReceiptReturn.getPassengerDetails().get(0).getTicketNumber());
    	assertEquals(ticketReceiptRsRow.getFirstNm(), ticketReceiptReturn.getPassengerDetails().get(0).getFirstName());
    	assertEquals(ticketReceiptRsRow.getLastNm(), ticketReceiptReturn.getPassengerDetails().get(0).getLastName());
    	assertEquals(ticketReceiptRsRow.getAadvantNbr(), ticketReceiptReturn.getPassengerDetails().get(0).getAdvantageNumber());
    	assertEquals(ticketReceiptRsRow.getLyltyOwnCd(), ticketReceiptReturn.getPassengerDetails().get(0).getLoyaltyOwnerCode());

    	assertEquals(ticketReceiptRsRow.getSegDeptDt(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentDepartureDate());
    	assertEquals(ticketReceiptRsRow.getSegDeptTm(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentDepartureTime());
    	assertEquals(ticketReceiptRsRow.getSegDeptArprtCd(), ticketReceiptReturn.getSegmentDetails().get(0).getDepartureAirport().getCode());
    	assertEquals(ticketReceiptRsRow.getSegArvltDt(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentArrivalDate());
    	assertEquals(ticketReceiptRsRow.getSegArvlTm(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentArrivalTime());
    	assertEquals(ticketReceiptRsRow.getSegArvlArprtCd(), ticketReceiptReturn.getSegmentDetails().get(0).getArrivalAirport().getCode());

    	assertEquals(ticketReceiptRsRow.getSegCouponStatusCd(), ticketReceiptReturn.getSegmentDetails().get(0).getSegmentStatus());
    	assertEquals(ticketReceiptRsRow.getSegOperatCarrierCd(), ticketReceiptReturn.getSegmentDetails().get(0).getCarrierCode());
    	assertEquals(ticketReceiptRsRow.getFlightNbr(), ticketReceiptReturn.getSegmentDetails().get(0).getFlightNumber());
    	assertEquals(ticketReceiptRsRow.getBookingClass(), ticketReceiptReturn.getSegmentDetails().get(0).getBookingClass());
    	assertEquals(ticketReceiptRsRow.getFareBase(), ticketReceiptReturn.getSegmentDetails().get(0).getFareBasis());
    	assertEquals("false", ticketReceiptReturn.getSegmentDetails().get(0).getReturnTrip());    }
    
    private void mockTicketReceipt() throws ParseException {
    	ticketReceiptMock = new TicketReceipt();
    	ticketReceiptMock.setAirlineAccountCode(Constants.AIRLN_ACCT_CD);
    	ticketReceiptMock.setTicketIssueDate(Constants.dateFormat.parse(Constants.TICKET_ISSUE_DT));
    	ticketReceiptMock.setDepartureDate(Constants.dateFormat.parse(Constants.DEP_DT));
    	ticketReceiptMock.setPnr(Constants.PNR);
    	ticketReceiptMock.setOriginAirport(getAirport(Constants.SEG_DEPT_ARPRT_CD, Constants.SEG_DEPT_ARPRT_NAME, Constants.SEG_DEPT_ARPRT_CITY, Constants.SEG_DEPT_ARPRT_STATE, Constants.SEG_DEPT_ARPRT_COUNTRY_CD, Constants.SEG_DEPT_ARPRT_COUNTRY_NM));
    	ticketReceiptMock.setDestinationAirport(getAirport(Constants.SEG_ARVL_ARPRT_CD, Constants.SEG_ARVL_ARPRT_NAME, Constants.SEG_ARVL_ARPRT_CITY, Constants.SEG_ARVL_ARPRT_STATE, Constants.SEG_ARVL_ARPRT_COUNTRY_CD, Constants.SEG_ARVL_ARPRT_COUNTRY_NM));

        PassengerDetail passengerDetail = new PassengerDetail();    	
        passengerDetail.setTicketNumber(Constants.TICKET_NBR);
        passengerDetail.setFirstName(Constants.FIRST_NM);
        passengerDetail.setLastName(Constants.LAST_NM);
        passengerDetail.setAdvantageNumber(Constants.AADVANT_NBR);
        passengerDetail.setLoyaltyOwnerCode(Constants.LYLTY_OWN_CD);
        
        ticketReceiptMock.getPassengerDetails().add(passengerDetail);
        addOneSegment();
    }
    
    private void addOneSegment() throws ParseException {
        SegmentDetail segmentDetail = new SegmentDetail();    	
        segmentDetail.setSegmentDepartureDate(Constants.dateFormat.parse(Constants.SEG_DEPT_DT));
        segmentDetail.setSegmentDepartureTime(Constants.SEG_DEPT_TM);
        segmentDetail.setDepartureAirport(getAirport(Constants.SEG_DEPT_ARPRT_CD, Constants.SEG_DEPT_ARPRT_NAME, Constants.SEG_DEPT_ARPRT_CITY, Constants.SEG_DEPT_ARPRT_STATE, Constants.SEG_DEPT_ARPRT_COUNTRY_CD, Constants.SEG_DEPT_ARPRT_COUNTRY_NM));
        segmentDetail.setSegmentArrivalDate(Constants.dateFormat.parse(Constants.SEG_ARVL_DT));
        segmentDetail.setSegmentArrivalTime(Constants.SEG_ARVL_TM);
        segmentDetail.setArrivalAirport(getAirport(Constants.SEG_ARVL_ARPRT_CD, Constants.SEG_ARVL_ARPRT_NAME, Constants.SEG_ARVL_ARPRT_CITY, Constants.SEG_ARVL_ARPRT_STATE, Constants.SEG_ARVL_ARPRT_COUNTRY_CD, Constants.SEG_ARVL_ARPRT_COUNTRY_NM));
        segmentDetail.setSegmentStatus(Constants.SEG_COUPON_STATUS_CD);
        segmentDetail.setCarrierCode(Constants.SEG_OPERAT_CARRIER_CD);
        segmentDetail.setFlightNumber(Constants.FLIGHT_NBR);
        segmentDetail.setBookingClass(Constants.BOOKING_CLASS);
        segmentDetail.setFareBasis(Constants.FARE_BASE);
        segmentDetail.setReturnTrip("false");
        
        ticketReceiptMock.getSegmentDetails().add(segmentDetail);    	
    }
    
    @Test
    public void mapResultSet() throws ParseException {

        Mockito.when(resultSet.next()).thenReturn(true, false); // first time return true and second time return false
        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn("001");
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(Constants.dateFormat.parse("2019-03-14").getTime()));
        final Date departureDate = Constants.dateFormat.parse("2019-09-30");
        final Date arrivalDate = Constants.dateFormat.parse("2019-09-30");
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(departureDate.getTime()));

        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn("MCO");
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn("MIA");
        Mockito.when(airportService.getAirport("MCO")).thenReturn(getAirport("MCO", "Orlando International", "Orlando", "FL", "USA", "United States"));
        Mockito.when(airportService.getAirport("MIA")).thenReturn(getAirport("MIA", "Miami International", "Miami", "FL", "USA", "United States"));
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");
        Mockito.when(resultSet.getString("AADVANT_NBR")).thenReturn("279RFY4");

        // mock passenger details
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("AADVANT_NBR")).thenReturn("279RFY4");
        Mockito.when(resultSet.getString("LYLTY_OWN_CD")).thenReturn("AA  ");

        // mock segment details
        Mockito.when(resultSet.getDate("SEG_DEPT_DT")).thenReturn(new java.sql.Date(departureDate.getTime()));
        Mockito.when(resultSet.getDate("SEG_ARVL_DT")).thenReturn(new java.sql.Date(arrivalDate.getTime()));
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn("MCO");
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn("MIA");
        Mockito.when(resultSet.getString("SEG_OPERAT_CARRIER_CD")).thenReturn("AA");
        final String segmentDepartureTime = "8:05:00";
        Mockito.when(resultSet.getString("SEG_DEPT_TM")).thenReturn(segmentDepartureTime);
        Mockito.when(resultSet.getTime("SEG_DEPT_TM")).thenReturn(Time.valueOf(segmentDepartureTime));
        final String segmentArrivalTime = "9:05:00";
        final String segmentStatus = "   USED   ";
        Mockito.when(resultSet.getString("SEG_ARVL_TM")).thenReturn(segmentArrivalTime);
        Mockito.when(resultSet.getString("SEG_COUPON_STATUS_CD")).thenReturn(segmentStatus);
        Mockito.when(resultSet.getString("FLIGHT_NBR")).thenReturn("1536");
        Mockito.when(resultSet.getString("BOOKING_CLASS")).thenReturn("B");
        Mockito.when(resultSet.getString("FARE_BASE")).thenReturn("QVAJZNB3");
        Mockito.when(resultSet.getString("COUPON_SEQ_NBR")).thenReturn("1");

        TicketReceipt item = ticketReceiptMapper.mapTicketReceipt(resultSet);

        // header
        assertThat(item.getAirlineAccountCode()).isEqualTo("001");
        assertThat(item.getTicketIssueDate()).isEqualTo(Constants.dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(departureDate);
        assertThat(item.getOriginAirport().getCode()).isEqualTo("MCO");
        assertThat(item.getDestinationAirport().getCode()).isEqualTo("MIA");
        assertThat(item.getOriginAirport().getName()).isEqualTo("Orlando International");
        assertThat(item.getDestinationAirport().getName()).isEqualTo("Miami International");
        assertThat(item.getPnr()).isEqualTo("MRYMPT");

        // passenger details
        assertThat(item.getPassengerDetails().size()).isEqualTo(1);
        PassengerDetail passengerDetail = item.getPassengerDetails().get(0);
        assertThat(passengerDetail.getTicketNumber()).isEqualTo("2335038507");
        assertThat(passengerDetail.getFirstName()).isEqualTo("SIMON");
        assertThat(passengerDetail.getLastName()).isEqualTo("TEST");
        assertThat(passengerDetail.getAdvantageNumber()).isEqualTo("279RFY4");
        assertThat(passengerDetail.getLoyaltyOwnerCode()).isEqualTo("AA");

        // segment details
        assertThat(item.getSegmentDetails().size()).isEqualTo(1);
        SegmentDetail segmentDetail = item.getSegmentDetails().get(0);
        assertThat(segmentDetail.getDepartureAirport().getName()).isEqualTo("Orlando International");
        assertThat(segmentDetail.getArrivalAirport().getName()).isEqualTo("Miami International");
        assertThat(segmentDetail.getSegmentDepartureDate()).isEqualTo(departureDate);
        assertThat(segmentDetail.getSegmentArrivalDate()).isEqualTo(arrivalDate);
        assertThat(segmentDetail.getDepartureAirport().getCode()).isEqualTo("MCO");
        assertThat(segmentDetail.getArrivalAirport().getCode()).isEqualTo("MIA");

        assertThat(segmentDetail.getSegmentDepartureTime()).isEqualTo(segmentDepartureTime);
        assertThat(segmentDetail.getSegmentArrivalTime()).isEqualTo(segmentArrivalTime);
        assertThat(segmentDetail.getSegmentStatus()).isEqualTo("USED");
        assertThat(segmentDetail.getFlightNumber()).isEqualTo("1536");
        assertThat(segmentDetail.getCarrierCode()).isEqualTo("AA");
        assertThat(segmentDetail.getBookingClass()).isEqualTo("B");
        assertThat(segmentDetail.getFareBasis()).isEqualTo("QVAJZNB3");
        assertThat(segmentDetail.getReturnTrip()).isEqualTo("false");
    }

    @Test
    public void mapResultSetWithNulls() throws ParseException {

        Mockito.when(resultSet.next()).thenReturn(true, false); // first time return true and second time return false
        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(Constants.dateFormat.parse("2019-03-14").getTime()));
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(Constants.dateFormat.parse("2019-09-30").getTime()));

        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn(null);
        Mockito.when(airportService.getAirport(null)).thenReturn(null);
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");
        Mockito.when(resultSet.getString("LYLTY_OWN_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("SEG_COUPON_STATUS_CD")).thenReturn(null);
        final String segmentDepartureTime = "8:05:00";
        Mockito.when(resultSet.getTime("SEG_DEPT_TM")).thenReturn(Time.valueOf(segmentDepartureTime));
        final Date departureDate = Constants.dateFormat.parse("2019-09-30");
        Mockito.when(resultSet.getDate("SEG_DEPT_DT")).thenReturn(new java.sql.Date(departureDate.getTime()));

        TicketReceipt item = ticketReceiptMapper.mapTicketReceipt(resultSet);
        SegmentDetail segmentDetail = new SegmentDetail();
        segmentDetail.setSegmentStatus(null);
        item.getSegmentDetails().add(segmentDetail);
        assertThat(item.getAirlineAccountCode()).isNull();
        assertThat(item.getTicketIssueDate()).isEqualTo(Constants.dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(Constants.dateFormat.parse("2019-09-30"));
        assertThat(item.getOriginAirport()).isNull();
        assertThat(item.getDestinationAirport()).isNull();
        assertThat(item.getPnr()).isEqualTo("MRYMPT");
        assertThat(item.getSegmentDetails().get(0).getSegmentStatus()).isEqualTo("");
    }

    public Airport getAirport(String code, String name, String city, String state, String countryCode, String countryName) {
        Airport airport = new Airport();
        airport.setCode(code);
        airport.setCity(city);
        airport.setCountryCode(countryCode);
        airport.setCountryName(countryName);
        airport.setName(name);
        airport.setStateCode(state);
        return airport;
    }
}
