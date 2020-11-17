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
                .segDeptDt(Constants.dateFormat.parse(Constants.SEG_DEPT_DT))
                .segDeptTm(Constants.SEG_DEPT_TM)
                .segDeptArprtCd(Constants.SEG_DEPT_ARPRT_CD)
                .segArvltDt(Constants.dateFormat.parse(Constants.SEG_ARVL_DT))
                .segArvlTm(Constants.SEG_ARVL_TM)
                .segArvlArprtCd(Constants.SEG_ARVL_ARPRT_CD)
                .segCouponStatusCd(Constants.SEG_COUPON_STATUS_CD)
                .segOperatCarrierCd(Constants.SEG_OPERAT_CARRIER_CD)
                .flightNbr(Constants.FLIGHT_NBR)
                .bookingClass(Constants.BOOKING_CLASS)
                .fareBase(Constants.FARE_BASE)
                .couponSeqNbr(Constants.COUPON_SEQ_NBR)

                .eqfnFareAmt(Constants.EQFN_FARE_AMOUNT)
                .eqfnFareCurrTypeCd(Constants.EQFN_FARE_CURRENCY_CODE)
                .fnumFareAmt(Constants.FNUM_FARE_AMOUNT)
                .fnumFareCurrTypeCd(Constants.FNUM_FARE_CURRENCY_CODE)
                .fareTdamAmt(Constants.FARE_TDAM_AMOUNT)

                .fopAcctNbrLast4(Constants.FOP_ACCTNBR_LAST4)
                .fopAmt(Constants.FOP_AMT)
                .fopCurrTypeCd(Constants.FOP_CURRTYPE_CD)
                .fopIssueDt(Constants.dateFormat.parse(Constants.FOP_ISSUE_DT))
                .fopTypeCd(Constants.FOP_TYPE_CD)
                .build();


    }
}
