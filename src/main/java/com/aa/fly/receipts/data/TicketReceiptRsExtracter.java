package com.aa.fly.receipts.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.TicketReceiptRsRow;

@Component
public class TicketReceiptRsExtracter {

	public List<TicketReceiptRsRow> extract(SqlRowSet sqlRowSet) {
		List<TicketReceiptRsRow> ticketReceiptRsRowList = null;
		
		if (sqlRowSet != null) {
			ticketReceiptRsRowList = new ArrayList<>();
			
	        while (sqlRowSet.next()) {
	        	
				TicketReceiptRsRow ticketReceiptRsRow = 
						TicketReceiptRsRow.builder()
						.airlnAcctCd(StringUtils.isNotBlank(sqlRowSet.getString("AIRLN_ACCT_CD")) ? 
								sqlRowSet.getString("AIRLN_ACCT_CD").trim() : null)
						.ticketNbr(sqlRowSet.getString("TICKET_NBR").trim())
						.ticketIssueDt(sqlRowSet.getDate("TICKET_ISSUE_DT"))
						.depDt(sqlRowSet.getDate("DEP_DT"))
						.firstNm(sqlRowSet.getString("FIRST_NM").trim())
						.lastNm(sqlRowSet.getString("LAST_NM").trim())
						.orgAtoCd(StringUtils.isNotBlank(sqlRowSet.getString("ORG_ATO_CD")) ? 
	                            sqlRowSet.getString("ORG_ATO_CD").trim() : "")
						.destAtoCd(StringUtils.isNotBlank(sqlRowSet.getString("DEST_ATO_CD")) ? 
	                            sqlRowSet.getString("DEST_ATO_CD").trim() : "")
						.pnr(sqlRowSet.getString("PNR").trim())
						.aadvantNbr(StringUtils.isNotBlank(sqlRowSet.getString("AADVANT_NBR")) ? 
	                            sqlRowSet.getString("AADVANT_NBR").trim() : null)
						.lyltyOwnCd(StringUtils.isNotBlank(sqlRowSet.getString("LYLTY_OWN_CD")) ? 
	                            sqlRowSet.getString("LYLTY_OWN_CD").trim() : null)
						.segDeptDt(sqlRowSet.getDate("SEG_DEPT_DT"))
						.segDeptTm(sqlRowSet.getString("SEG_DEPT_TM"))
						.segDeptArprtCd(sqlRowSet.getString("SEG_DEPT_ARPRT_CD"))
						.segArvltDt(sqlRowSet.getDate("SEG_ARVL_DT"))
						.segArvlTm(sqlRowSet.getString("SEG_ARVL_TM"))
						.segArvlArprtCd(sqlRowSet.getString("SEG_ARVL_ARPRT_CD"))
						.segCouponStatusCd(StringUtils.isNotBlank(sqlRowSet.getString("SEG_COUPON_STATUS_CD")) ? 
	                            sqlRowSet.getString("SEG_COUPON_STATUS_CD").trim() : null)
						.segOperatCarrierCd(StringUtils.isNotBlank(sqlRowSet.getString("SEG_OPERAT_CARRIER_CD")) ? 
	                            sqlRowSet.getString("SEG_OPERAT_CARRIER_CD") : null)
						.flightNbr(sqlRowSet.getString("FLIGHT_NBR"))
						.bookingClass(StringUtils.isNotBlank(sqlRowSet.getString("BOOKING_CLASS")) ? 
	                            sqlRowSet.getString("BOOKING_CLASS") : null)
						.fareBase(StringUtils.isNotBlank(sqlRowSet.getString("FARE_BASE")) ? 
	                            sqlRowSet.getString("FARE_BASE").trim() : null)
						.couponSeqNbr(sqlRowSet.getString("COUPON_SEQ_NBR"))

						.build();
		
				ticketReceiptRsRowList.add(ticketReceiptRsRow);
	        }
		}
		
		return ticketReceiptRsRowList;
	}
}

