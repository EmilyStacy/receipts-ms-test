package com.aa.fly.receipts.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;

@Component
public class TicketReceiptMapper {

    @Autowired
    private AirportRepository airportRepository;

    public TicketReceipt mapTicketReceipt(SqlRowSet rs) {

        TicketReceipt ticketReceipt = new TicketReceipt();
        int rowCount = 0;

        while (rs.next()) {
            if (rowCount == 0) {
                ticketReceipt.setAirlineAccountCode(rs.getString("AIRLN_ACCT_CD") != null ? rs.getString("AIRLN_ACCT_CD").trim() : null);
                ticketReceipt.setTicketNumber(rs.getString("TICKET_NBR"));
                ticketReceipt.setTicketIssueDate(rs.getDate("TICKET_ISSUE_DT"));
                ticketReceipt.setDepartureDate(rs.getDate("DEP_DT"));
                ticketReceipt.setFirstName(rs.getString("FIRST_NM"));
                ticketReceipt.setLastName(rs.getString("LAST_NM"));
                ticketReceipt.setOriginAirportCode(rs.getString("ORG_ATO_CD") != null ? rs.getString("ORG_ATO_CD").trim() : null);
                ticketReceipt.setDestinationAirportCode(rs.getString("DEST_ATO_CD") != null ? rs.getString("DEST_ATO_CD").trim() : null);
                ticketReceipt.setOriginAirport(airportRepository.getAirportName(ticketReceipt.getOriginAirportCode()));
                ticketReceipt.setDestinationAirport(airportRepository.getAirportName(ticketReceipt.getDestinationAirportCode()));
                ticketReceipt.setPnr(rs.getString("PNR"));
            }

            ticketReceipt.getSegmentDetails().add(mapSegmentDetails(rs, rowCount));
            rowCount++;
        }
        return ticketReceipt;
    }

    private SegmentDetail mapSegmentDetails(SqlRowSet rs, int rowCount) {
        SegmentDetail segmentDetail = new SegmentDetail();
        segmentDetail.setSegmentDepartureDate(rs.getDate("SEG_DEPT_DT"));
        segmentDetail.setSegmentDepartureAirportCode(rs.getString("SEG_DEPT_ARPRT_CD") != null ? rs.getString("SEG_DEPT_ARPRT_CD").trim() : null);
        segmentDetail.setSegmentArrivalAirportCode(rs.getString("SEG_ARVL_ARPRT_CD") != null ? rs.getString("SEG_ARVL_ARPRT_CD").trim() : null);
        segmentDetail.setSegmentDepartureAirportName(airportRepository.getAirportName(segmentDetail.getSegmentDepartureAirportCode()));
        segmentDetail.setSegmentArrivalAirportName(airportRepository.getAirportName(segmentDetail.getSegmentArrivalAirportCode()));
        segmentDetail.setSegmentDepartureTime(rs.getString("SEG_DEPT_TM"));
        segmentDetail.setSegmentArrivalTime(rs.getString("SEG_ARVL_TM"));
        segmentDetail.setCarrierCode("AA");
        segmentDetail.setFlightNumber(rs.getString("FLIGHT_NBR") != null ? rs.getString("FLIGHT_NBR").trim() : null);
        segmentDetail.setBookingClass(rs.getString("BOOKING_CLASS") != null ? rs.getString("BOOKING_CLASS").trim() : null);
        segmentDetail.setFareBasis(rs.getString("FARE_BASE") != null ? rs.getString("FARE_BASE").trim() : null);
        segmentDetail.setReturnTrip(rs.getString("COUPON_SEQ_NBR") != null && ("1").equals(rs.getString("COUPON_SEQ_NBR")) && rowCount != 0 ? true : false);

        return segmentDetail;
    }

}
