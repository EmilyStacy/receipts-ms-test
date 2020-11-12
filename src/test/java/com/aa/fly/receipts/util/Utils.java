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
		.ticketNbr(Constants.TICKET_NBR)
		.firstNm(Constants.FIRST_NM)
		.lastNm(Constants.LAST_NM)
		.aadvantNbr(Constants.AADVANT_NBR)
		.lyltyOwnCd(Constants.LYLTY_OWN_CD)
		.build();
	}
}
