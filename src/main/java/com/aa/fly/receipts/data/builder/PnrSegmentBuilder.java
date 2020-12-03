package com.aa.fly.receipts.data.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.AirportService;

@Component
public class PnrSegmentBuilder{
   @Autowired
   private AirportService airportService;
   
   public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow, int rowIndex) {
		SegmentDetail segmentDetail = new SegmentDetail();
		
		segmentDetail.setSegmentDepartureDate(ticketReceiptRsRow.getSegDeptDt());
		segmentDetail.setSegmentDepartureTime(ticketReceiptRsRow.getSegDeptTm());
		segmentDetail.setSegmentArrivalDate(ticketReceiptRsRow.getSegArvltDt());
		segmentDetail.setSegmentArrivalTime(ticketReceiptRsRow.getSegArvlTm());
		segmentDetail.setSegmentStatus(ticketReceiptRsRow.getSegCouponStatusCd());
		segmentDetail.setCarrierCode(ticketReceiptRsRow.getSegOperatCarrierCd());
		segmentDetail.setFlightNumber(ticketReceiptRsRow.getFlightNbr());
		segmentDetail.setBookingClass(ticketReceiptRsRow.getBookingClass());
		segmentDetail.setFareBasis(ticketReceiptRsRow.getFareBase());
		segmentDetail.setDepartureAirport(airportService.getAirport(ticketReceiptRsRow.getSegDeptArprtCd()));
		segmentDetail.setArrivalAirport(airportService.getAirport(ticketReceiptRsRow.getSegArvlArprtCd()));
		
		String returnTrip = ("1").equals(ticketReceiptRsRow.getCouponSeqNbr()) && rowIndex != 0 ? "true" : "false";
		segmentDetail.setReturnTrip(returnTrip);
		
		ticketReceipt.getSegmentDetails().add(segmentDetail);
		
        return ticketReceipt;
   }
}