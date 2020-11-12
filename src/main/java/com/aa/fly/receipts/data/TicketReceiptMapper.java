package com.aa.fly.receipts.data;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.data.builder.PassengerBuilder;
import com.aa.fly.receipts.data.builder.PnrHeaderBuilder;
import com.aa.fly.receipts.data.builder.PnrSegmentBuilder;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.AirportService;
import com.aa.fly.receipts.service.DataBuilderService;
import java.util.Objects;

@Component
public class TicketReceiptMapper {

	private static final String FLIGHT_NBR = "FLIGHT_NBR";
    private static final String SEG_DEPT_DT = "SEG_DEPT_DT";
    private static final String SEG_DEPT_TM = "SEG_DEPT_TM";
	
    @Autowired
    private AirportService airportService;
    
    @Autowired
    private PnrHeaderBuilder pnrHeaderBuilder;
    
    @Autowired
    private PassengerBuilder passengerBuilder;    
    
    @Autowired
    private PnrSegmentBuilder pnrSegmentBuilder;    

    public TicketReceipt mapTicketReceipt(List<TicketReceiptRsRow> ticketReceiptRsRowList) {

        TicketReceipt ticketReceiptReturn = null;
        int rowCount = 0;
        String lastDepartureDateTime = "";
        String currentDepartureDateTime = null;

        Iterator<TicketReceiptRsRow> iterator = ticketReceiptRsRowList.iterator();
        TicketReceiptRsRow ticketReceiptRsRow = null;

        while (iterator.hasNext()) {
        	ticketReceiptRsRow = iterator.next();
        	
            if (rowCount == 0) {
            	ticketReceiptReturn = pnrHeaderBuilder.build(new TicketReceipt(), ticketReceiptRsRow);
            	ticketReceiptReturn = passengerBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
            }

            currentDepartureDateTime = Objects.requireNonNull(ticketReceiptRsRow.getSegDeptDt().toString())
            		.concat(Objects.requireNonNull(ticketReceiptRsRow.getSegDeptTm()));

            if (!lastDepartureDateTime.equals(currentDepartureDateTime))
            {
            	ticketReceiptReturn = pnrSegmentBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
                lastDepartureDateTime = currentDepartureDateTime;
            }
            
            rowCount++;
        }
        return ticketReceiptReturn;
    }
    
    public TicketReceipt mapTicketReceipt(SqlRowSet rs) {

        TicketReceipt ticketReceipt = new TicketReceipt();
        int rowCount = 0;
        String lastDepartureDateTime = "";
        String currentDepartureDateTime = null;

        while (rs.next()) {
            if (rowCount == 0) {
                ticketReceipt.setAirlineAccountCode(StringUtils.isNotBlank(rs.getString("AIRLN_ACCT_CD")) ? rs.getString("AIRLN_ACCT_CD").trim() : null);
                ticketReceipt.setTicketIssueDate(rs.getDate("TICKET_ISSUE_DT"));
                ticketReceipt.setDepartureDate(rs.getDate("DEP_DT"));
                ticketReceipt.setOriginAirport(airportService.getAirport(StringUtils.isNotBlank(rs.getString("ORG_ATO_CD")) ? rs.getString("ORG_ATO_CD").trim() : null));
                ticketReceipt.setDestinationAirport(airportService.getAirport(StringUtils.isNotBlank(rs.getString("DEST_ATO_CD")) ? rs.getString("DEST_ATO_CD").trim() : null));
                ticketReceipt.setPnr(rs.getString("PNR").trim());
                		
                PassengerDetail passengerDetail = new PassengerDetail();
                passengerDetail.setTicketNumber(rs.getString("TICKET_NBR"));
                passengerDetail.setFirstName(rs.getString("FIRST_NM"));
                passengerDetail.setLastName(rs.getString("LAST_NM"));
                passengerDetail.setAdvantageNumber(rs.getString("AADVANT_NBR"));
                passengerDetail.setLoyaltyOwnerCode(StringUtils.isNotBlank(rs.getString("LYLTY_OWN_CD")) ? rs.getString("LYLTY_OWN_CD").trim() : null);

                ticketReceipt.getPassengerDetails().add(passengerDetail);
            }

            currentDepartureDateTime = Objects.requireNonNull(rs.getDate(SEG_DEPT_DT)).toString().concat(Objects.requireNonNull(rs.getTime(SEG_DEPT_TM)).toString());

            if (!lastDepartureDateTime.equalsIgnoreCase(currentDepartureDateTime))
            {
                ticketReceipt.getSegmentDetails().add(mapSegmentDetails(rs, rowCount));
                lastDepartureDateTime = currentDepartureDateTime;
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
