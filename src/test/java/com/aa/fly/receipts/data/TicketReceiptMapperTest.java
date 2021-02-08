package com.aa.fly.receipts.data;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.text.ParseException;
import java.util.*;
import com.aa.fly.receipts.data.adjuster.PassengerTaxXFAdjuster;
import com.aa.fly.receipts.data.adjuster.PassengerTaxZPAdjuster;
import com.aa.fly.receipts.data.adjuster.PassengerTotalAdjuster;
import com.aa.fly.receipts.data.builder.*;

import com.aa.fly.receipts.domain.FormOfPayment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.exception.BulkTicketException;
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

    @Mock
    private PassengerFareTaxFeeBuilder passengerFareTaxFeeBuilder;

	@Mock
	private PassengerTaxFeeItemBuilder passengerTaxFeeItemBuilder;

    @Mock
    private PassengerFopBuilder passengerFopBuilder;

	@Mock
	private PassengerTaxXFAdjuster passengerTaxXFAdjuster;

    @Mock
	private PassengerTaxZPAdjuster passengerTaxZPAdjuster;

	@Mock
	private PassengerTotalAdjuster passengerTotalAdjuster;

	@Mock
	private PassengerAncillaryFopBuilder passengerAncillaryFopBuilder;

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
    public void testMapTicketReceipt_Resultset_Two_Rows_FOP_CA() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeCode("CA");
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopAmount("10.00");
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeDescription("CASH/CHECK");    	
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
    	ticketReceiptRsRow.setFopSeqId("2");
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    	
    	assertNotNull(ticketReceiptReturn);
    	assertEquals("CASH/CHECK", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_Two_Rows_FOP_CCBA() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
    	ticketReceiptRsRow.setFopSeqId("2");
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    	
    	assertNotNull(ticketReceiptReturn);
    	assertEquals(Constants.FOP_TYPE_DESCRIPTION, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_One_Row_FOP_EX() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeCode("EX");
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopAmount("1.00");
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeDescription("Exchange");
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
    	ticketReceiptRsRow.setFopTypeCd("EX");
    	ticketReceiptRsRow.setFopAmt("1.00");
    	
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    	
    	assertNotNull(ticketReceiptReturn);
    	assertEquals("Exchange", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    }

	@Test
	public void testAdjustadjustFormOfPaymentsIfExchanged_Two_Rows_threeTicketFOP_oneAnclryFOP() throws ParseException {
		ticketReceiptMock = Utils.mockTicketReceipt();
		Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
		FormOfPayment anclryFOP = new FormOfPayment();
		anclryFOP.setFopAmount("100.00");
		anclryFOP.setFopCurrencyCode("USD");
		anclryFOP.setFopAccountNumberLast4("0000");
		anclryFOP.setFopTypeDescription("VISA ends in 0000");
		anclryFOP.setFopTypeCode("CCIK");
		FormOfPayment nullAmtFop = new FormOfPayment();
		nullAmtFop.setFopAmount(null);
		nullAmtFop.setFopCurrencyCode("USD");
		nullAmtFop.setFopAccountNumberLast4("0000");
		nullAmtFop.setFopTypeDescription("VISA ends in 0000");
		nullAmtFop.setFopTypeCode("CCIK");
		FormOfPayment zeroAmtFop = new FormOfPayment();
		zeroAmtFop.setFopAmount("0.00");
		zeroAmtFop.setFopCurrencyCode("USD");
		zeroAmtFop.setFopAccountNumberLast4("0000");
		zeroAmtFop.setFopTypeDescription("VISA ends in 0000");
		zeroAmtFop.setFopTypeCode("CCIK");
		Utils.addOneAncillary(anclryFOP);
		anclryFOP.setFopIssueDate(Constants.dateFormat.parse(Constants.ANCLRY_ISSUE_DATE));
		nullAmtFop.setFopIssueDate(Constants.dateFormat.parse(Constants.ANCLRY_ISSUE_DATE));
		zeroAmtFop.setFopIssueDate(Constants.dateFormat.parse(Constants.ANCLRY_ISSUE_DATE));
		ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(anclryFOP);
		ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(nullAmtFop);
		ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().add(zeroAmtFop);
		ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeCode("EX");
		ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopAmount("1.00");
		ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeDescription("Exchange");

		ticketReceiptRsRowList.add(ticketReceiptRsRow);
		TicketReceiptRsRow ticketReceiptRsRow2 = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow2);
		ticketReceiptRsRow2.setFopSeqId("3");
		ticketReceiptRsRow2.setAnclryFopTypeCd("test");
		ticketReceiptRsRowList.add(ticketReceiptRsRow2);

		ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
		assertEquals("Exchange",ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
		assertEquals("VISA ends in 0000",ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(1).getFopTypeDescription() );
	}
    
    @Test
    public void testMapTicketReceipt_Resultset_One_Row_FOP_EF() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeCode("EF");
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopAmount("0.00");
    	ticketReceiptMock.getPassengerDetails().get(0).getFormOfPayments().get(0).setFopTypeDescription("Exchange");
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
    	ticketReceiptRsRow.setFopTypeCd("EF");
    	ticketReceiptRsRow.setFopAmt("0.00");
    	
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);

    	ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);
    	
    	assertNotNull(ticketReceiptReturn);
    	assertEquals("Exchange", ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    }

    @Test
	public void testMapTicketReceipt_Resultset_AnclryFop_Null() throws ParseException {
		ticketReceiptMock = Utils.mockTicketReceipt();
		Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
		ticketReceiptRsRow.setAnclryFopAmt(null);
		ticketReceiptRsRow.setAnclryFopCurrTypeCd(null);
		ticketReceiptRsRow.setAnclryFopAcctNbrLast4(null);
		ticketReceiptRsRow.setAnclryFopTypeCd(null);
		ticketReceiptRsRowList.add(ticketReceiptRsRow);

		ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);

		assertNotNull(ticketReceiptReturn);
		for(FormOfPayment fop: ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments()){
			assertEquals(0, fop.getAncillaries().size());
		}
	}

	@Test
	public void testMapTicketReceipt_Resultset_AnclryFop_Zero() throws ParseException {
		ticketReceiptMock = Utils.mockTicketReceipt();
		Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
		ticketReceiptRsRow.setAnclryFopAmt("00.00");
		ticketReceiptRsRow.setAnclryFopCurrTypeCd("USD2");
		ticketReceiptRsRow.setAnclryFopAcctNbrLast4("0000");
		ticketReceiptRsRow.setAnclryFopTypeCd("CCBA");
		ticketReceiptRsRowList.add(ticketReceiptRsRow);

		ticketReceiptReturn = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);

		assertNotNull(ticketReceiptReturn);
		for(FormOfPayment fop: ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments()){
			assertEquals(0, fop.getAncillaries().size());
		}
	}

    @Test
    public void testMapTicketReceipt_Resultset_One_Row() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
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
    	
    	assertEquals(ticketReceiptRsRow.getFopIssueDt(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopIssueDate());
    	assertEquals(ticketReceiptRsRow.getFopTypeCd(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeCode());
    	assertEquals(Constants.FOP_TYPE_DESCRIPTION, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    	assertEquals(ticketReceiptRsRow.getFopAcctNbrLast4(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAccountNumberLast4());
    	assertEquals(ticketReceiptRsRow.getFopAmt(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals(ticketReceiptRsRow.getFopCurrTypeCd(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopCurrencyCode());
    }

    @Test
    public void testMapTicketReceipt_Resultset_Two_Rows_Same_Segment() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);

		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
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

    	assertEquals(1, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().size());
    	assertEquals(ticketReceiptRsRow.getFopIssueDt(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopIssueDate());
    	assertEquals(ticketReceiptRsRow.getFopTypeCd(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeCode());
    	assertEquals(Constants.FOP_TYPE_DESCRIPTION, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    	assertEquals(ticketReceiptRsRow.getFopAcctNbrLast4(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAccountNumberLast4());
    	assertEquals(ticketReceiptRsRow.getFopAmt(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals(ticketReceiptRsRow.getFopCurrTypeCd(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopCurrencyCode());
    }
    
    @Test
    public void testMapTicketReceipt_Resultset_Three_Rows_Two_Segments() throws ParseException {
    	ticketReceiptMock = Utils.mockTicketReceipt();
    	Utils.addOneSegment(ticketReceiptMock);
        ticketReceiptMock.getSegmentDetails().get(1).setSegmentDepartureDate(Constants.dateFormat.parse(SEG_DEPT_DT2));
        ticketReceiptMock.getSegmentDetails().get(1).setSegmentDepartureTime(SEG_DEPT_TM2);
        ticketReceiptMock.getSegmentDetails().get(1).setReturnTrip("true");

    	Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
    	Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxZPAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
    	
    	ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
    	ticketReceiptRsRowList.add(ticketReceiptRsRow);
    	
    	TicketReceiptRsRow ticketReceiptRsRow2 = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow2);
    	ticketReceiptRsRow2.setCouponSeqNbr("2");
    	ticketReceiptRsRowList.add(ticketReceiptRsRow2);
    	
    	TicketReceiptRsRow ticketReceiptRsRow3 = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow3);
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

    	assertEquals(1, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().size());
    	assertEquals(ticketReceiptRsRow.getFopIssueDt(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopIssueDate());
    	assertEquals(ticketReceiptRsRow.getFopTypeCd(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeCode());
    	assertEquals(Constants.FOP_TYPE_DESCRIPTION, ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    	assertEquals(ticketReceiptRsRow.getFopAcctNbrLast4(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAccountNumberLast4());
    	assertEquals(ticketReceiptRsRow.getFopAmt(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
    	assertEquals(ticketReceiptRsRow.getFopCurrTypeCd(), ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopCurrencyCode());
    }

	@Test
	public void testZPAdjuster_withZP() throws ParseException {
		ticketReceiptMock = Utils.mockTicketReceipt();
		Mockito.when(pnrHeaderBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(pnrSegmentBuilder.build(any(), any(), anyInt())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFareTaxFeeBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxFeeItemBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTaxXFAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerAncillaryFopBuilder.build(any(), any())).thenReturn(ticketReceiptMock);
		Mockito.when(passengerTotalAdjuster.adjust(any())).thenReturn(ticketReceiptMock);
		ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
		Utils.addAncillaryToTicketReceiptRow(ticketReceiptRsRow);
		ticketReceiptRsRowList.add(ticketReceiptRsRow);

		ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);

		verify(passengerTaxZPAdjuster,times(1)).adjust(ticketReceiptMock);
	}
}
