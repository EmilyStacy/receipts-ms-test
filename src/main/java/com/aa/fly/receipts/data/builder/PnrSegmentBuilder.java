package com.aa.fly.receipts.data.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.AirportService;
import com.aa.fly.receipts.service.DataBuilderService;

@Component
public class PnrSegmentBuilder implements DataBuilderService {
	
	@Autowired
	private AirportService airportService;

	public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow, int rowIndex) {
		TicketReceipt ticketReceiptOverload = build(ticketReceipt, ticketReceiptRsRow);
		
        String returnTrip = ("1").equals(ticketReceiptRsRow.getCouponSeqNbr()) && rowIndex != 0 ? "true" : "false";
        ticketReceiptOverload.getSegmentDetails().get(ticketReceiptOverload.getSegmentDetails().size() - 1).setReturnTrip(returnTrip);
		
		return ticketReceiptOverload;
	}

	@Override
	public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {
		
        SegmentDetail segmentDetail = new SegmentDetail();

        segmentDetail.setSegmentDepartureDate(ticketReceiptRsRow.getSegDeptDt());
        segmentDetail.setSegmentDepartureTime(ticketReceiptRsRow.getSegDeptTm());
        segmentDetail.setDepartureAirport(airportService.getAirport(ticketReceiptRsRow.getSegDeptArprtCd()));
        segmentDetail.setSegmentArrivalDate(ticketReceiptRsRow.getSegArvltDt());
        segmentDetail.setSegmentArrivalTime(ticketReceiptRsRow.getSegArvlTm());
        segmentDetail.setArrivalAirport(airportService.getAirport(ticketReceiptRsRow.getSegArvlArprtCd()));
        segmentDetail.setSegmentStatus(ticketReceiptRsRow.getSegCouponStatusCd());
        segmentDetail.setCarrierCode(ticketReceiptRsRow.getSegOperatCarrierCd());
        segmentDetail.setFlightNumber(ticketReceiptRsRow.getFlightNbr());
        segmentDetail.setBookingClass(ticketReceiptRsRow.getBookingClass());
        segmentDetail.setFareBasis(ticketReceiptRsRow.getFareBase());
        
        ticketReceipt.getSegmentDetails().add(segmentDetail);
        return ticketReceipt;
	}
}

