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
	public void testExtract_SqlRowSet_One_Row() throws Exception {
        Mockito.when(sqlRowSet.next()).thenReturn(true).thenReturn(false); // first time return true
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
	}
}
