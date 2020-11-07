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
						.build();
				ticketReceiptRsRowList.add(ticketReceiptRsRow);
	        }
		}
		
		return ticketReceiptRsRowList;
	}
}
