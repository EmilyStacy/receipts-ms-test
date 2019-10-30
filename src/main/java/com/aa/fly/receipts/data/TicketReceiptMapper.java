package com.aa.fly.receipts.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.AirportService;

@Component
public class TicketReceiptMapper {

    @Autowired
    private AirportService airportService;

    public TicketReceipt mapTicketReceipt(SqlRowSet rs) {

        TicketReceipt ticketReceipt = new TicketReceipt();
        int rowCount = 0;

        while (rs.next()) {
            if (rowCount == 0) {
                ticketReceipt.setAirlineAccountCode(rs.getString("AIRLN_ACCT_CD") != null ? rs.getString("AIRLN_ACCT_CD").trim() : null);
                ticketReceipt.setTicketIssueDate(rs.getDate("TICKET_ISSUE_DT"));
                ticketReceipt.setDepartureDate(rs.getDate("DEP_DT"));
                ticketReceipt.setOriginAirport(airportService.getAirport(rs.getString("ORG_ATO_CD") != null ? rs.getString("ORG_ATO_CD").trim() : null));
                ticketReceipt.setDestinationAirport(airportService.getAirport(rs.getString("DEST_ATO_CD") != null ? rs.getString("DEST_ATO_CD").trim() : null));
                ticketReceipt.setPnr(rs.getString("PNR"));

                PassengerDetail passengerDetail = new PassengerDetail();
                passengerDetail.setTicketNumber(rs.getString("TICKET_NBR"));
                passengerDetail.setFirstName(rs.getString("FIRST_NM"));
                passengerDetail.setLastName(rs.getString("LAST_NM"));
                passengerDetail.setAdvantageNumber(rs.getString("AADVANT_NBR"));

                ticketReceipt.getPassengerDetails().add(passengerDetail);
            }

            ticketReceipt.getSegmentDetails().add(mapSegmentDetails(rs, rowCount));
            rowCount++;
        }
        return ticketReceipt;
    }

    private SegmentDetail mapSegmentDetails(SqlRowSet rs, int rowCount) {
        SegmentDetail segmentDetail = new SegmentDetail();
        segmentDetail.setSegmentDepartureDate(rs.getDate("SEG_DEPT_DT"));
        segmentDetail.setSegmentArrivalDate(rs.getDate("SEG_ARVL_DT"));
        segmentDetail.setDepartureAirport(airportService.getAirport(rs.getString("SEG_DEPT_ARPRT_CD") != null ? rs.getString("SEG_DEPT_ARPRT_CD").trim() : null));
        segmentDetail.setArrivalAirport(airportService.getAirport(rs.getString("SEG_ARVL_ARPRT_CD") != null ? rs.getString("SEG_ARVL_ARPRT_CD").trim() : null));
        segmentDetail.setSegmentDepartureTime(rs.getString("SEG_DEPT_TM"));
        segmentDetail.setSegmentArrivalTime(rs.getString("SEG_ARVL_TM"));
        segmentDetail.setCarrierCode(rs.getString("SEG_OPERAT_CARRIER_CD") != null ? rs.getString("SEG_OPERAT_CARRIER_CD").trim() : null);
        segmentDetail.setFlightNumber(rs.getString("FLIGHT_NBR") != null ? rs.getString("FLIGHT_NBR").trim() : null);
        segmentDetail.setBookingClass(rs.getString("BOOKING_CLASS") != null ? rs.getString("BOOKING_CLASS").trim() : null);
        segmentDetail.setFareBasis(rs.getString("FARE_BASE") != null ? rs.getString("FARE_BASE").trim() : null);
        segmentDetail.setReturnTrip(rs.getString("COUPON_SEQ_NBR") != null && ("1").equals(rs.getString("COUPON_SEQ_NBR")) && rowCount != 0 ? "true" : "false");

        return segmentDetail;
    }

}
