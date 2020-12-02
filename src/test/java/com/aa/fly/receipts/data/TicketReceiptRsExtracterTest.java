package com.aa.fly.receipts.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.TicketReceiptRsRow;

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketReceiptRsExtracterTest {
	static final String AIRLN_ACCT_CD = "001";
	static final String TICKET_NBR = "2100000000";
	static final String TICKET_ISSUE_DT = "2020-10-10";
	static final String DEP_DT = "2020-12-10";
	static final String FIRST_NM = "John";
	static final String LAST_NM = "Smith";
	static final String ORG_ATO_CD = "DFW";
	static final String DEST_ATO_CD = "LAX";
	static final String PNR = "ABCDEF";
	static final String AADVANT_NBR = "1A2B3D4F";
	static final String LYLTY_OWN_CD = "AA";

	static final String SEG_DEPT_DT = "2020-10-15";
	static final String SEG_DEPT_TM = "12:50:00";
	static final String SEG_DEPT_ARPRT_CD = "DFW";
	static final String SEG_ARVL_DT = "2020-10-15";
	static final String SEG_ARVL_TM = "14:34:00";
	static final String SEG_ARVL_ARPRT_CD = "LAX";
	static final String SEG_COUPON_STATUS_CD = "OK";
	static final String SEG_OPERAT_CARRIER_CD = "AA";
	static final String FLIGHT_NBR = "1071";
	static final String BOOKING_CLASS = "B";
	static final String FARE_BASE = "OVBZZNB5";
	static final String COUPON_SEQ_NBR = "1";
	
	static final String FOP_ISSUE_DT = "2020-10-10";
	static final String FOP_TYPE_CD = "CCIK";
	static final String FOP_AMT = "105.20";
	static final String FOP_SEQ_ID = "1";
	static final String FOP_ACCT_NBR_LAST4 = "1234";
	static final String FOP_CURR_TYPE_CD = "USD2";
	
	static final String FNUM_FARE_AMT = "34300";
	static final String FNUM_FARE_CURR_TYPE_CD = "USD2";
	static final String EQFN_FARE_AMT = "0";
	static final String EQFN_FARE_CURR_TYPE_CD = "";
	static final String FARE_TDAM_AMT = "41704";
	static final String TCN_BULK_IND = "";
	
	static final String TAX_CD_SEQ_ID = "1";
	static final String TAX_CD = "AY";
	static final String CITY_CD = "STL";
	static final String TAX_AMT = "11.20";
	static final String TAX_CURR_TYPE_CD = "USD2";
	
	static final String ANCLRY_DOC_NBR = "617097598";
	static final String ANCLRY_ISSUE_DT = "2020-10-10";
	static final String ANCLRY_PROD_CD = "089";
	static final String ANCLRY_PROD_NM = "PAID LFB UPGRADE";
	static final String ANCLRY_PRICE_LCL_CURNCY_AMT = "42.83";
	static final String ANCLRY_PRICE_LCL_CURNCY_CD = "USD";
	static final String ANCLRY_SLS_CURNCY_AMT = "46.04";
	static final String ANCLRY_SLS_CURNCY_CD = "USD";
	static final String ANCLRY_FOP_AMT = "4604";
	static final String ANCLRY_FOP_TYPE_CD = "CCBA";
	static final String ANCLRY_FOP_ACCT_NBR_LAST4 = "6170";
	static final String ANCLRY_FOP_CURR_TYPE_CD = "USD2";

	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Mock
    private SqlRowSet sqlRowSet;
    
    private TicketReceiptRsExtracter ticketReceiptRsExtracter = new TicketReceiptRsExtracter();
    private List<TicketReceiptRsRow> ticketReceiptRsRowList = null;

	@Test
	public void testExtract_SqlRowSet_Null() throws Exception {
		ticketReceiptRsExtracter.extract(null);
		assertNull(ticketReceiptRsRowList);
	}
	
	@Test
	public void testExtract_SqlRowSet_AirlineCode_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false); // first time return true
		this.mockFields();
        Mockito.when(sqlRowSet.getString("AIRLN_ACCT_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getAirlnAcctCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_AirlineCode_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false); // first time return true
		this.mockFields();
        Mockito.when(sqlRowSet.getString("AIRLN_ACCT_CD")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getAirlnAcctCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_OrgAtoCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("ORG_ATO_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals("", ticketReceiptRsRowList.get(0).getOrgAtoCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_DestAtoCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("DEST_ATO_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals("", ticketReceiptRsRowList.get(0).getDestAtoCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_AadvantNbr_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("AADVANT_NBR")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getAadvantNbr());
	}	
	
	@Test
	public void testExtract_SqlRowSet_AadvantNbr_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("AADVANT_NBR")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getAadvantNbr());
	}	
	
	@Test
	public void testExtract_SqlRowSet_LyltyOwnCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("LYLTY_OWN_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getLyltyOwnCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_LyltyOwnCd_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("LYLTY_OWN_CD")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getLyltyOwnCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_SegCouponStatusCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("SEG_COUPON_STATUS_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getSegCouponStatusCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_SegCouponStatusCd_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("SEG_COUPON_STATUS_CD")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getSegCouponStatusCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_SegOperatCarrierCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("SEG_OPERAT_CARRIER_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getSegOperatCarrierCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_SegOperatCarrierCd_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("SEG_OPERAT_CARRIER_CD")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getSegOperatCarrierCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_BookingClass_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("BOOKING_CLASS")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getBookingClass());
	}	
	
	@Test
	public void testExtract_SqlRowSet_BookingClass_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("BOOKING_CLASS")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getBookingClass());
	}		
	
	@Test
	public void testExtract_SqlRowSet_FareBase_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("FARE_BASE")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getFareBase());
	}	
	
	@Test
	public void testExtract_SqlRowSet_FareBase_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("FARE_BASE")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getFareBase());
	}	
	
	@Test
	public void testExtract_SqlRowSet_FopTypeCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("FOP_TYPE_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals("", ticketReceiptRsRowList.get(0).getFopTypeCd());
	}	
		
	@Test
	public void testExtract_SqlRowSet_FopAacctNbrLast4_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("FOP_ACCT_NBR_LAST4")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals("", ticketReceiptRsRowList.get(0).getFopAcctNbrLast4());
	}
	
	@Test
	public void testExtract_SqlRowSet_FopCurrTypeCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("FOP_CURR_TYPE_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals("", ticketReceiptRsRowList.get(0).getFopCurrTypeCd());
	}
	
	@Test
	public void testExtract_SqlRowSet_CityCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("CITY_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals("", ticketReceiptRsRowList.get(0).getCityCd());
	}
	
	@Test
	public void testExtract_SqlRowSet_AnclryDocNbr_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("ANCLRY_DOC_NBR")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals("", ticketReceiptRsRowList.get(0).getAnclryDocNbr());
	}

	@Test
	public void testExtract_SqlRowSet_One_Row() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false); 
		this.mockFields();
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(AIRLN_ACCT_CD, ticketReceiptRsRowList.get(0).getAirlnAcctCd());
		assertEquals(TICKET_NBR, ticketReceiptRsRowList.get(0).getTicketNbr());
		assertEquals(dateFormat.parse(TICKET_ISSUE_DT), ticketReceiptRsRowList.get(0).getTicketIssueDt());
		assertEquals(dateFormat.parse(DEP_DT), ticketReceiptRsRowList.get(0).getDepDt());
		assertEquals(FIRST_NM, ticketReceiptRsRowList.get(0).getFirstNm());
		assertEquals(LAST_NM, ticketReceiptRsRowList.get(0).getLastNm());
		assertEquals(ORG_ATO_CD, ticketReceiptRsRowList.get(0).getOrgAtoCd());
		assertEquals(DEST_ATO_CD, ticketReceiptRsRowList.get(0).getDestAtoCd());
		assertEquals(PNR, ticketReceiptRsRowList.get(0).getPnr());
		assertEquals(AADVANT_NBR, ticketReceiptRsRowList.get(0).getAadvantNbr());
		assertEquals(LYLTY_OWN_CD, ticketReceiptRsRowList.get(0).getLyltyOwnCd());
		
		assertEquals(dateFormat.parse(SEG_DEPT_DT), ticketReceiptRsRowList.get(0).getSegDeptDt());
		assertEquals(SEG_DEPT_TM, ticketReceiptRsRowList.get(0).getSegDeptTm());
		assertEquals(SEG_DEPT_ARPRT_CD, ticketReceiptRsRowList.get(0).getSegDeptArprtCd());
		assertEquals(dateFormat.parse(SEG_ARVL_DT), ticketReceiptRsRowList.get(0).getSegArvltDt());
		assertEquals(SEG_ARVL_TM, ticketReceiptRsRowList.get(0).getSegArvlTm());
		assertEquals(SEG_ARVL_ARPRT_CD, ticketReceiptRsRowList.get(0).getSegArvlArprtCd());

		assertEquals(SEG_COUPON_STATUS_CD, ticketReceiptRsRowList.get(0).getSegCouponStatusCd());
		assertEquals(SEG_OPERAT_CARRIER_CD, ticketReceiptRsRowList.get(0).getSegOperatCarrierCd());
		assertEquals(FLIGHT_NBR, ticketReceiptRsRowList.get(0).getFlightNbr());
		assertEquals(BOOKING_CLASS, ticketReceiptRsRowList.get(0).getBookingClass());
		assertEquals(FARE_BASE, ticketReceiptRsRowList.get(0).getFareBase());
		assertEquals(COUPON_SEQ_NBR, ticketReceiptRsRowList.get(0).getCouponSeqNbr());

		assertEquals(dateFormat.parse(FOP_ISSUE_DT), ticketReceiptRsRowList.get(0).getFopIssueDt());
		assertEquals(FOP_TYPE_CD, ticketReceiptRsRowList.get(0).getFopTypeCd());
		assertEquals(FOP_AMT, ticketReceiptRsRowList.get(0).getFopAmt());
		assertEquals(FOP_SEQ_ID, ticketReceiptRsRowList.get(0).getFopSeqId());
		assertEquals(FOP_ACCT_NBR_LAST4, ticketReceiptRsRowList.get(0).getFopAcctNbrLast4());
		assertEquals(FOP_CURR_TYPE_CD, ticketReceiptRsRowList.get(0).getFopCurrTypeCd());
		
		assertEquals(FNUM_FARE_AMT, ticketReceiptRsRowList.get(0).getFnumFareAmt());
		assertEquals(FNUM_FARE_CURR_TYPE_CD, ticketReceiptRsRowList.get(0).getFnumFareCurrTypeCd());
		assertEquals(EQFN_FARE_AMT, ticketReceiptRsRowList.get(0).getEqfnFareAmt());
		assertEquals(EQFN_FARE_CURR_TYPE_CD, ticketReceiptRsRowList.get(0).getEqfnFareCurrTypeCd());
		assertEquals(FARE_TDAM_AMT, ticketReceiptRsRowList.get(0).getFareTdamAmt());
		assertEquals(TCN_BULK_IND, ticketReceiptRsRowList.get(0).getTcnBulkInd());
		
		assertEquals(TAX_CD_SEQ_ID, ticketReceiptRsRowList.get(0).getTaxCdSeqId());
		assertEquals(TAX_CD, ticketReceiptRsRowList.get(0).getTaxCd());
		assertEquals(CITY_CD, ticketReceiptRsRowList.get(0).getCityCd());
		assertEquals(TAX_AMT, ticketReceiptRsRowList.get(0).getTaxAmt());
		assertEquals(TAX_CURR_TYPE_CD, ticketReceiptRsRowList.get(0).getTaxCurrTypeCd());
		
		assertEquals(ANCLRY_DOC_NBR, ticketReceiptRsRowList.get(0).getAnclryDocNbr());
		assertEquals(ANCLRY_ISSUE_DT, dateFormat.parse(ANCLRY_ISSUE_DT), ticketReceiptRsRowList.get(0).getAnclryIssueDt());
		assertEquals(ANCLRY_PROD_CD, ticketReceiptRsRowList.get(0).getAnclryProdCd());
		assertEquals(ANCLRY_PROD_NM, ticketReceiptRsRowList.get(0).getAnclryProdNm());
		assertEquals(ANCLRY_PRICE_LCL_CURNCY_AMT, ticketReceiptRsRowList.get(0).getAnclryPriceLclCurncyAmt());
		assertEquals(ANCLRY_PRICE_LCL_CURNCY_CD, ticketReceiptRsRowList.get(0).getAnclryPriceLclCurncyCd());
		assertEquals(ANCLRY_SLS_CURNCY_AMT, ticketReceiptRsRowList.get(0).getAnclrySlsCurncyAmt());
		assertEquals(ANCLRY_SLS_CURNCY_CD, ticketReceiptRsRowList.get(0).getAnclrySlsCurncyCd());
		assertEquals(ANCLRY_FOP_AMT, ticketReceiptRsRowList.get(0).getAnclryFopAmt());
		assertEquals(ANCLRY_FOP_TYPE_CD, ticketReceiptRsRowList.get(0).getAnclryFopTypeCd());
		assertEquals(ANCLRY_FOP_ACCT_NBR_LAST4, ticketReceiptRsRowList.get(0).getAnclryFopAcctNbrLast4());
		assertEquals(ANCLRY_FOP_CURR_TYPE_CD, ticketReceiptRsRowList.get(0).getAnclryFopCurrTypeCd());
	}

	@Test
	public void testExtract_SqlRowSet_Two_Rows() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // first time return true and second time return true
		this.mockFields();
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(2, ticketReceiptRsRowList.size());
	}
	
	private void mockFields() throws InvalidResultSetAccessException, ParseException {
        Mockito.when(sqlRowSet.getString("AIRLN_ACCT_CD")).thenReturn(AIRLN_ACCT_CD);
        Mockito.when(sqlRowSet.getString("TICKET_NBR")).thenReturn(TICKET_NBR);
        Mockito.when(sqlRowSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse(TICKET_ISSUE_DT).getTime()));		
        Mockito.when(sqlRowSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(dateFormat.parse(DEP_DT).getTime()));		
        Mockito.when(sqlRowSet.getString("FIRST_NM")).thenReturn(FIRST_NM);
        Mockito.when(sqlRowSet.getString("LAST_NM")).thenReturn(LAST_NM);
        Mockito.when(sqlRowSet.getString("ORG_ATO_CD")).thenReturn(ORG_ATO_CD);
        Mockito.when(sqlRowSet.getString("DEST_ATO_CD")).thenReturn(DEST_ATO_CD);
        Mockito.when(sqlRowSet.getString("PNR")).thenReturn(PNR);
        Mockito.when(sqlRowSet.getString("AADVANT_NBR")).thenReturn(AADVANT_NBR);
        Mockito.when(sqlRowSet.getString("LYLTY_OWN_CD")).thenReturn(LYLTY_OWN_CD);

        Mockito.when(sqlRowSet.getDate("SEG_DEPT_DT")).thenReturn(new java.sql.Date(dateFormat.parse(SEG_DEPT_DT).getTime()));
        Mockito.when(sqlRowSet.getString("SEG_DEPT_TM")).thenReturn(SEG_DEPT_TM);
        Mockito.when(sqlRowSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn(SEG_DEPT_ARPRT_CD);
        Mockito.when(sqlRowSet.getDate("SEG_ARVL_DT")).thenReturn(new java.sql.Date(dateFormat.parse(SEG_ARVL_DT).getTime()));
        Mockito.when(sqlRowSet.getString("SEG_ARVL_TM")).thenReturn(SEG_ARVL_TM);
        Mockito.when(sqlRowSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn(SEG_ARVL_ARPRT_CD);
        
        Mockito.when(sqlRowSet.getString("SEG_COUPON_STATUS_CD")).thenReturn(SEG_COUPON_STATUS_CD);
        Mockito.when(sqlRowSet.getString("SEG_OPERAT_CARRIER_CD")).thenReturn(SEG_OPERAT_CARRIER_CD);
        Mockito.when(sqlRowSet.getString("FLIGHT_NBR")).thenReturn(FLIGHT_NBR);
        Mockito.when(sqlRowSet.getString("BOOKING_CLASS")).thenReturn(BOOKING_CLASS);
        Mockito.when(sqlRowSet.getString("FARE_BASE")).thenReturn(FARE_BASE);
        Mockito.when(sqlRowSet.getString("COUPON_SEQ_NBR")).thenReturn(COUPON_SEQ_NBR);

        Mockito.when(sqlRowSet.getDate("FOP_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse(FOP_ISSUE_DT).getTime()));
        Mockito.when(sqlRowSet.getString("FOP_TYPE_CD")).thenReturn(FOP_TYPE_CD);
        Mockito.when(sqlRowSet.getString("FOP_AMT")).thenReturn(FOP_AMT);
        Mockito.when(sqlRowSet.getString("FOP_SEQ_ID")).thenReturn(FOP_SEQ_ID);
        Mockito.when(sqlRowSet.getString("FOP_ACCT_NBR_LAST4")).thenReturn(FOP_ACCT_NBR_LAST4);
        Mockito.when(sqlRowSet.getString("FOP_CURR_TYPE_CD")).thenReturn(FOP_CURR_TYPE_CD);

        Mockito.when(sqlRowSet.getString("FNUM_FARE_AMT")).thenReturn(FNUM_FARE_AMT);
        Mockito.when(sqlRowSet.getString("FNUM_FARE_CURR_TYPE_CD")).thenReturn(FNUM_FARE_CURR_TYPE_CD);
        Mockito.when(sqlRowSet.getString("EQFN_FARE_AMT")).thenReturn(EQFN_FARE_AMT);
        Mockito.when(sqlRowSet.getString("EQFN_FARE_CURR_TYPE_CD")).thenReturn(EQFN_FARE_CURR_TYPE_CD);
        Mockito.when(sqlRowSet.getString("FARE_TDAM_AMT")).thenReturn(FARE_TDAM_AMT);
        Mockito.when(sqlRowSet.getString("TCN_BULK_IND")).thenReturn(TCN_BULK_IND);

        Mockito.when(sqlRowSet.getString("TAX_CD_SEQ_ID")).thenReturn(TAX_CD_SEQ_ID);
        Mockito.when(sqlRowSet.getString("TAX_CD")).thenReturn(TAX_CD);
        Mockito.when(sqlRowSet.getString("CITY_CD")).thenReturn(CITY_CD);
        Mockito.when(sqlRowSet.getString("TAX_AMT")).thenReturn(TAX_AMT);
        Mockito.when(sqlRowSet.getString("TAX_CURR_TYPE_CD")).thenReturn(TAX_CURR_TYPE_CD);
        
        Mockito.when(sqlRowSet.getString("ANCLRY_DOC_NBR")).thenReturn(ANCLRY_DOC_NBR);
        Mockito.when(sqlRowSet.getDate("ANCLRY_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse(ANCLRY_ISSUE_DT).getTime()));
        Mockito.when(sqlRowSet.getString("ANCLRY_PROD_CD")).thenReturn(ANCLRY_PROD_CD);
        Mockito.when(sqlRowSet.getString("ANCLRY_PROD_NM")).thenReturn(ANCLRY_PROD_NM);
        Mockito.when(sqlRowSet.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")).thenReturn(ANCLRY_PRICE_LCL_CURNCY_AMT);
        Mockito.when(sqlRowSet.getString("ANCLRY_PRICE_LCL_CURNCY_CD")).thenReturn(ANCLRY_PRICE_LCL_CURNCY_CD);
        Mockito.when(sqlRowSet.getString("ANCLRY_SLS_CURNCY_AMT")).thenReturn(ANCLRY_SLS_CURNCY_AMT);
        Mockito.when(sqlRowSet.getString("ANCLRY_SLS_CURNCY_CD")).thenReturn(ANCLRY_SLS_CURNCY_CD);
        Mockito.when(sqlRowSet.getString("ANCLRY_FOP_AMT")).thenReturn(ANCLRY_FOP_AMT);
        Mockito.when(sqlRowSet.getString("ANCLRY_FOP_TYPE_CD")).thenReturn(ANCLRY_FOP_TYPE_CD);
        Mockito.when(sqlRowSet.getString("ANCLRY_FOP_ACCT_NBR_LAST4")).thenReturn(ANCLRY_FOP_ACCT_NBR_LAST4);
        Mockito.when(sqlRowSet.getString("ANCLRY_FOP_CURR_TYPE_CD")).thenReturn(ANCLRY_FOP_CURR_TYPE_CD);
	}
}
