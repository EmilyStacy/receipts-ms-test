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
   
   private String fnumFareAmt;
   private String fnumFareCurrTypeCd;
   private String eqfnFareAmt;
   private String eqfnFareCurrTypeCd;
   private String fareTdamAmt;
   private String tcnBulkInd;
   
   private String taxCdSeqId;
   private String taxCd;
   private String cityCd;
   private String taxAmt;
   private String taxCurrTypeCd;
   
   private String anclryDocNbr;
   private Date anclryIssueDt;
   private String anclryProdCd;
   private String anclryProdNm;
   private String anclryPriceLclCurncyAmt;
   private String anclryPriceLclCurncyCd;
   private String anclrySlsCurncyAmt;
   private String anclrySlsCurncyCd;
   private String anclryFopAmt;
   private String anclryFopTypeCd;
   private String anclryFopAcctNbrLast4;
   private String anclryFopCurrTypeCd;
}
