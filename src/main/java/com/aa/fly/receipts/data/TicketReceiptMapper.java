package com.aa.fly.receipts.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;

public class TicketReceiptMapper implements RowMapper<TicketReceipt> {

    @Override
    public TicketReceipt mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        TicketReceipt ticketReceipt = new TicketReceipt();
        
        ticketReceipt.setAirlineAccountCode(rs.getString("AIRLN_ACCT_CD") != null ? rs.getString("AIRLN_ACCT_CD").trim() : null);
        ticketReceipt.setTicketNumber(rs.getString("TICKET_NBR"));
        ticketReceipt.setTicketIssueDate(rs.getDate("TICKET_ISSUE_DT"));
        ticketReceipt.setDepartureDate(rs.getDate("DEP_DT"));
        ticketReceipt.setFirstName(rs.getString("FIRST_NM"));
        ticketReceipt.setLastName(rs.getString("LAST_NM"));
        ticketReceipt.setOriginAirportCode(rs.getString("ORG_ATO_CD") != null ? rs.getString("ORG_ATO_CD").trim() : null);
        ticketReceipt.setDestinationAirportCode(rs.getString("DEST_ATO_CD") != null ? rs.getString("DEST_ATO_CD").trim() : null);
        ticketReceipt.setOriginAirport(rs.getString("ORG_ATO_NM"));
        ticketReceipt.setDestinationAirport(rs.getString("DEST_ATO_NM"));
        ticketReceipt.setPnr(rs.getString("PNR"));

        ticketReceipt.setSegmentDetails(mapSegmentDetails(rs));
        return ticketReceipt;
    }

    private List<SegmentDetail> mapSegmentDetails(ResultSet rs) throws SQLException {
        SegmentDetail segmentDetail = new SegmentDetail();
        segmentDetail.setSegmentDepartureAirportName(rs.getString("SEG_DEPT_ARPRT_NM") != null ? rs.getString("SEG_DEPT_ARPRT_NM").trim() : null);
        segmentDetail.setSegmentArrivalAirportName(rs.getString("SEG_ARVL_ARPRT_NM") != null ? rs.getString("SEG_ARVL_ARPRT_NM").trim() : null);
        segmentDetail.setSegmentDepartureDate(rs.getDate("SEG_DEPT_DT"));
        segmentDetail.setSegmentDepartureAirportCode(rs.getString("SEG_DEPT_ARPRT_CD") != null ? rs.getString("SEG_DEPT_ARPRT_CD").trim() : null);
        segmentDetail.setSegmentArrivalAirportCode(rs.getString("SEG_ARVL_ARPRT_CD") != null ? rs.getString("SEG_ARVL_ARPRT_CD").trim() : null);
        segmentDetail.setSegmentDepartureTime(rs.getTime("SEG_DEPT_TM"));
        segmentDetail.setSegmentArrivalTime(rs.getTime("SEG_ARVL_TM"));
        segmentDetail.setCarrierCode("AA");
        segmentDetail.setFlightNumber(rs.getString("FLIGHT_NBR") != null ? rs.getString("FLIGHT_NBR").trim() : null);
        segmentDetail.setBookingClass(rs.getString("BOOKING_CLASS") != null ? rs.getString("BOOKING_CLASS").trim() : null);
        segmentDetail.setFareBasis(rs.getString("FARE_BASE") != null ? rs.getString("FARE_BASE").trim() : null);

        List<SegmentDetail> segmentDetails = new ArrayList<>();
        segmentDetails.add(segmentDetail);
        return segmentDetails;
    }

}
