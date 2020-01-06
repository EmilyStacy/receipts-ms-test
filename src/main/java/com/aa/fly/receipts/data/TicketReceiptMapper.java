package com.aa.fly.receipts.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.FormOfPaymentKey;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.Tax;
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

            ticketReceipt.getSegmentDetails().add(mapSegmentDetails(rs, rowCount));
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
        segmentDetail.setSegmentArrivalTime(rs.getString("SEG_ARVL_TM"));
        segmentDetail.setCarrierCode(StringUtils.isNotBlank(rs.getString("SEG_OPERAT_CARRIER_CD")) ? rs.getString("SEG_OPERAT_CARRIER_CD").trim() : null);
        segmentDetail.setFlightNumber(StringUtils.isNotBlank(rs.getString("FLIGHT_NBR")) ? rs.getString("FLIGHT_NBR").trim() : null);
        segmentDetail.setBookingClass(StringUtils.isNotBlank(rs.getString("BOOKING_CLASS")) ? rs.getString("BOOKING_CLASS").trim() : null);
        segmentDetail.setFareBasis(StringUtils.isNotBlank(rs.getString("FARE_BASE")) ? rs.getString("FARE_BASE").trim() : null);
        segmentDetail.setReturnTrip(StringUtils.isNotBlank(rs.getString("COUPON_SEQ_NBR")) && ("1").equals(rs.getString("COUPON_SEQ_NBR")) && rowCount != 0 ? "true" : "false");

        return segmentDetail;
    }

}
