package com.aa.fly.receipts.util;

import java.text.ParseException;

import com.aa.fly.receipts.domain.TicketReceiptRsRow;

public class Utils {
	
	public static TicketReceiptRsRow mockTicketReceiptRsRow() throws ParseException {
		return TicketReceiptRsRow.builder()
		.airlnAcctCd(Constants.AIRLN_ACCT_CD)
		.ticketIssueDt(Constants.dateFormat.parse(Constants.TICKET_ISSUE_DT))
		.depDt(Constants.dateFormat.parse(Constants.DEP_DT))
		.pnr(Constants.PNR)
		.orgAtoCd(Constants.ORG_ATO_CD)
		.destAtoCd(Constants.DEST_ATO_CD)
		.build();
	}
}
