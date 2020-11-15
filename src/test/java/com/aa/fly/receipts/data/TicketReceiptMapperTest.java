package com.aa.fly.receipts.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.data.builder.PassengerBuilder;
import com.aa.fly.receipts.data.builder.PassengerFareTaxFeeBuilder;
import com.aa.fly.receipts.data.builder.PnrHeaderBuilder;
import com.aa.fly.receipts.data.builder.PnrSegmentBuilder;
import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.AirportService;
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;
import com.aa.fly.receipts.exception.BulkTicketException;

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

    @Mock
    private PassengerFareTaxFeeBuilder passengerFareTaxFeeBuilder;

    @InjectMocks
    private TicketReceiptMapper ticketReceiptMapper;

    private TicketReceipt ticketReceiptMock, ticketReceiptReturn;
    private TicketReceiptRsRow ticketReceiptRsRow;
    private List<TicketReceiptRsRow> ticketReceiptRsRowList = new ArrayList<>();
    
    private static final String SEG_DEPT_DT2 = "2020-10-11";
    private static final String SEG_DEPT_TM2 = "06:50:00";
    
    @Test(expected = BulkTicketException.class)
    public void testMapTicketReceipt_Bulk_Ticket_BT() throws ParseException {
    	
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
    	ticketReceiptRsRow.setTcnBulkInd("BT");
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    }
    
    @Test(expected = BulkTicketException.class)
    public void testMapTicketReceipt_Bulk_Ticket_WT() throws ParseException {
    	
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
    	ticketReceiptRsRow.setTcnBulkInd("WT");
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_One_Row() throws ParseException {
    	this.mockTicketReceipt();
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	
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
    	
    	assertEquals(Constants.BASE_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount());
    	assertEquals(Constants.BASE_FARE_CURRENCY_CODE, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode());
    	assertEquals(Constants.TOTAL_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
    	assertEquals(Constants.TAX_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxFareAmount());    	
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_Two_Rows_Same_Segment() throws ParseException {
    	this.mockTicketReceipt();
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	
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
    	
    	assertEquals(Constants.BASE_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount());
    	assertEquals(Constants.BASE_FARE_CURRENCY_CODE, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode());
    	assertEquals(Constants.TOTAL_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
    	assertEquals(Constants.TAX_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxFareAmount());    	
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_Three_Rows_Two_Segments() throws ParseException {
    	this.mockTicketReceipt();
        this.addOneSegment();
        ticketReceiptMock.getSegmentDetails().get(1).setSegmentDepartureDate(Constants.dateFormat.parse(SEG_DEPT_DT2));
        ticketReceiptMock.getSegmentDetails().get(1).setSegmentDepartureTime(SEG_DEPT_TM2);
        ticketReceiptMock.getSegmentDetails().get(1).setReturnTrip("true");

    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();    
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);
    	
    	TicketReceiptRsRow ticketReceiptRsRow2 = Utils.mockTicketReceiptRsRow();
    	ticketReceiptRsRow2.setCouponSeqNbr("2");
    	ticketReceiptRsRowList.add(ticketReceiptRsRow2);
    	
    	TicketReceiptRsRow ticketReceiptRsRow3 = Utils.mockTicketReceiptRsRow();
    	ticketReceiptRsRow3.setSegDeptDt(Constants.dateFormat.parse(SEG_DEPT_DT2));
    	ticketReceiptRsRow3.setSegDeptTm(SEG_DEPT_TM2);
    	ticketReceiptRsRow3.setCouponSeqNbr("1");
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
    	assertEquals("false", ticketReceiptReturn.getSegmentDetails().get(0).getReturnTrip());
    	
    	assertEquals(ticketReceiptRsRowList.get(2).getSegDeptDt(), ticketReceiptReturn.getSegmentDetails().get(1).getSegmentDepartureDate());
    	assertEquals(ticketReceiptRsRowList.get(2).getSegDeptTm(), ticketReceiptReturn.getSegmentDetails().get(1).getSegmentDepartureTime());    	
    	assertEquals("true", ticketReceiptReturn.getSegmentDetails().get(1).getReturnTrip());
    	
    	assertEquals(Constants.BASE_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount());
    	assertEquals(Constants.BASE_FARE_CURRENCY_CODE, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode());
    	assertEquals(Constants.TOTAL_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
    	assertEquals(Constants.TAX_FARE_AMOUNT, ticketReceiptReturn.getPassengerDetails().get(0).getFareTaxesFees().getTaxFareAmount());    	
    }
    
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
        
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setBaseFareAmount(Constants.BASE_FARE_AMOUNT);
        fareTaxesFees.setBaseFareCurrencyCode(Constants.BASE_FARE_CURRENCY_CODE);
        fareTaxesFees.setTotalFareAmount(Constants.TOTAL_FARE_AMOUNT);
        fareTaxesFees.setTaxFareAmount(Constants.TAX_FARE_AMOUNT);
        
        passengerDetail.setFareTaxesFees(fareTaxesFees);
        
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
