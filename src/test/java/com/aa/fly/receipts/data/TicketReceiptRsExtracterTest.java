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
		assertEquals(null, ticketReceiptRsRowList.get(0).getOrgAtoCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_OrgAtoCd_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("ORG_ATO_CD")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getOrgAtoCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_DestAtoCd_Null() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("DEST_ATO_CD")).thenReturn(null);
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getDestAtoCd());
	}	
	
	@Test
	public void testExtract_SqlRowSet_DestAtoCd_Empty() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
		this.mockFields();
        Mockito.when(sqlRowSet.getString("DEST_ATO_CD")).thenReturn("");
		
        ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
		assertNotNull(ticketReceiptRsRowList);
		assertEquals(1, ticketReceiptRsRowList.size());
		assertEquals(null, ticketReceiptRsRowList.get(0).getDestAtoCd());
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
    }
}

