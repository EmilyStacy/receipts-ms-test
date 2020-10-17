package com.aa.fly.receipts.data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.AirportService;

@Component
public class TicketReceiptMapper {

	private static final String FLIGHT_NBR = "FLIGHT_NBR";
	
    @Autowired
    private AirportService airportService;

    public TicketReceipt mapTicketReceipt(SqlRowSet rs) {

        TicketReceipt ticketReceipt = new TicketReceipt();
        int rowCount = 0;
        String lastFlightNbr = "";
        String currFlightNbr = null;

        while (rs.next()) {
            if (rowCount == 0) {
                ticketReceipt.setAirlineAccountCode(StringUtils.isNotBlank(rs.getString("AIRLN_ACCT_CD")) ? rs.getString("AIRLN_ACCT_CD").trim() : null);
                ticketReceipt.setTicketIssueDate(rs.getDate("TICKET_ISSUE_DT"));
                ticketReceipt.setDepartureDate(rs.getDate("DEP_DT"));
                ticketReceipt.setOriginAirport(airportService.getAirport(StringUtils.isNotBlank(rs.getString("ORG_ATO_CD")) ? rs.getString("ORG_ATO_CD").trim() : null));
                ticketReceipt.setDestinationAirport(airportService.getAirport(StringUtils.isNotBlank(rs.getString("DEST_ATO_CD")) ? rs.getString("DEST_ATO_CD").trim() : null));
                ticketReceipt.setPnr(rs.getString("PNR"));
                		
                PassengerDetail passengerDetail = new PassengerDetail();
                passengerDetail.setTicketNumber(rs.getString("TICKET_NBR"));
                passengerDetail.setFirstName(rs.getString("FIRST_NM"));
                passengerDetail.setLastName(rs.getString("LAST_NM"));
                passengerDetail.setAdvantageNumber(rs.getString("AADVANT_NBR"));
                passengerDetail.setLoyaltyOwnerCode(StringUtils.isNotBlank(rs.getString("LYLTY_OWN_CD")) ? rs.getString("LYLTY_OWN_CD").trim() : null);

                ticketReceipt.getPassengerDetails().add(passengerDetail);
            }

            currFlightNbr = StringUtils.isNotBlank(rs.getString(FLIGHT_NBR)) ? rs.getString(FLIGHT_NBR).trim() : null;

            if (lastFlightNbr != null && !lastFlightNbr.equalsIgnoreCase(currFlightNbr))
            {
                ticketReceipt.getSegmentDetails().add(mapSegmentDetails(rs, rowCount));
                lastFlightNbr = currFlightNbr;
            }
            rowCount++;
        }
        return ticketReceipt;
    }

    private SegmentDetail mapSegmentDetails(SqlRowSet rs, int rowCount) {
        SegmentDetail segmentDetail = new SegmentDetail();
        segmentDetail.setSegmentDepartureDate(rs.getDate("SEG_DEPT_DT"));
        segmentDetail.setSegmentArrivalDate(rs.getDate("SEG_ARVL_DT"));
        segmentDetail.setDepartureAirport(airportService.getAirport(StringUtils.isNotBlank(rs.getString("SEG_DEPT_ARPRT_CD")) ? rs.getString("SEG_DEPT_ARPRT_CD").trim() : null));
        segmentDetail.setArrivalAirport(airportService.getAirport(StringUtils.isNotBlank(rs.getString("SEG_ARVL_ARPRT_CD")) ? rs.getString("SEG_ARVL_ARPRT_CD").trim() : null));
        segmentDetail.setSegmentDepartureTime(rs.getString("SEG_DEPT_TM"));
        segmentDetail.setSegmentStatus(StringUtils.isNotBlank(rs.getString("SEG_COUPON_STATUS_CD")) ? rs.getString("SEG_COUPON_STATUS_CD").trim() : "");
        segmentDetail.setSegmentArrivalTime(rs.getString("SEG_ARVL_TM"));
        segmentDetail.setCarrierCode(StringUtils.isNotBlank(rs.getString("SEG_OPERAT_CARRIER_CD")) ? rs.getString("SEG_OPERAT_CARRIER_CD").trim() : null);
        segmentDetail.setFlightNumber(StringUtils.isNotBlank(rs.getString(FLIGHT_NBR)) ? rs.getString(FLIGHT_NBR).trim() : null);
        segmentDetail.setBookingClass(StringUtils.isNotBlank(rs.getString("BOOKING_CLASS")) ? rs.getString("BOOKING_CLASS").trim() : null);
        segmentDetail.setFareBasis(StringUtils.isNotBlank(rs.getString("FARE_BASE")) ? rs.getString("FARE_BASE").trim() : null);
        segmentDetail.setReturnTrip(StringUtils.isNotBlank(rs.getString("COUPON_SEQ_NBR")) && ("1").equals(rs.getString("COUPON_SEQ_NBR")) && rowCount != 0 ? "true" : "false");

        return segmentDetail;
    }

}
