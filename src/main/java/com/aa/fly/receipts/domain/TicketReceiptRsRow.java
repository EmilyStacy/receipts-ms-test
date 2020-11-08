package com.aa.fly.receipts.domain;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class maps field by field to Mosaic result set.
 **/
@ToString
@Getter
@Setter
@Builder
public class TicketReceiptRsRow
{
   private String airlnAcctCd;
   private String ticketNbr;
   private Date ticketIssueDt;
   private Date depDt;
   private String firstNm;
   private String lastNm;
}
/*
.append("    ORG_ATO_CD, \n")
.append("    DEST_ATO_CD, \n")
.append("    PNR, \n")
.append("    AADVANT_NBR, \n")
.append("    LYLTY_OWN_CD, \n")
// =================== trip details =======================
.append("    SEG_DEPT_DT, \n")
.append("    SEG_DEPT_TM, \n")
.append("    SEG_DEPT_ARPRT_CD, \n")
.append("    SEG_ARVL_DT, \n")
.append("    SEG_ARVL_TM, \n")
.append("    SEG_ARVL_ARPRT_CD, \n")
.append("    SEG_COUPON_STATUS_CD, \n")
.append("    SEG_OPERAT_CARRIER_CD, \n")
.append("    FLIGHT_NBR, \n")
.append("    BOOKING_CLASS, \n")
.append("    FARE_BASE, \n")
.append("    COUPON_SEQ_NBR, \n")
// =================== cost - fop =======================
.append("    FOP_ISSUE_DT, \n")
.append("    FOP_TYPE_CD, \n")
.append("    FOP_AMT, \n")
.append("    FOP_SEQ_ID, \n")
.append("    FOP_ACCT_NBR_LAST4, \n")
.append("    FOP_CURR_TYPE_CD, \n")
// =================== cost - fare =======================
.append("    FNUM_FARE_AMT, \n")
.append("    FNUM_FARE_CURR_TYPE_CD, \n")
.append("    EQFN_FARE_AMT, \n")
.append("    EQFN_FARE_CURR_TYPE_CD, \n")
.append("    FARE_TDAM_AMT, \n")
.append("    TCN_BULK_IND, \n")
// =================== cost - tax =======================
.append("    TAX_CD_SEQ_ID, \n")
.append("    TAX_CD, \n")
.append("    CITY_CD, \n")
.append("    TAX_AMT, \n")
.append("    TAX_CURR_TYPE_CD, \n")
// =================== cost - ancillary =======================
.append("    ANCLRY_DOC_NBR, \n")
.append("    ANCLRY_ISSUE_DT, \n")
.append("    ANCLRY_PROD_CD, \n")
.append("    ANCLRY_PROD_NM, \n")
.append("    ANCLRY_PRICE_LCL_CURNCY_AMT, \n")
.append("    ANCLRY_PRICE_LCL_CURNCY_CD, \n")
.append("    ANCLRY_SLS_CURNCY_AMT, \n")
.append("    ANCLRY_SLS_CURNCY_CD, \n")
.append("    ANCLRY_FOP_AMT, \n")
.append("    ANCLRY_FOP_TYPE_CD, \n")
.append("    ANCLRY_FOP_ACCT_NBR_LAST4, \n")
.append("    ANCLRY_FOP_CURR_TYPE_CD \n")
*/