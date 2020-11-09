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
   private String orgAtoCd;
   private String destAtoCd;
   private String pnr;
   private String aadvantNbr;
   private String lyltyOwnCd;
   
   private Date segDeptDt;
   private String segDeptTm;
   private String segDeptArprtCd;
   private Date segArvltDt;
   private String segArvlTm;
   private String segArvlArprtCd;
   
   private String segCouponStatusCd;
   private String segOperatCarrierCd;
   private String flightNbr;
   private String bookingClass;
   private String fareBase;
   private String couponSeqNbr;

   private Date fopIssueDt;
   private String fopTypeCd;
   private String fopAmt;
   private String fopSeqId;
   private String fopAcctNbrLast4;
   private String fopCurrTypeCd;
}
/*

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