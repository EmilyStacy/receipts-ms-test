package com.aa.fly.receipts.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.AirportService;

@Component
public class TicketReceiptMapper {

    @Autowired
    private AirportService airportService;

    private Map<String, String> fopTypeMap;

    @Autowired
    public void setFopTypeMap(Map<String, String> fopTypeMap) {
        this.fopTypeMap = fopTypeMap;
    }

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
                passengerDetail.setLoyaltyOwnerCode(rs.getString("LYLTY_OWN_CD") != null ? rs.getString("LYLTY_OWN_CD").trim() : null);

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

    public PassengerDetail mapCostDetails(SqlRowSet rs, PassengerDetail passengerDetail) {
        List<FormOfPayment> formOfPayments = new ArrayList<>();
        boolean isFirst = true;

        while (rs.next()) {
            FormOfPayment formOfPayment = new FormOfPayment();
            formOfPayment.setFopAccountNumberLast4(rs.getString("FOP_ACCT_NBR_LAST4") != null ? rs.getString("FOP_ACCT_NBR_LAST4").trim() : null);
            formOfPayment.setFopIssueDate(rs.getDate("FOP_ISSUE_DT"));
            formOfPayment.setFopAmount(rs.getString("FOP_AMT") != null ? rs.getString("FOP_AMT").trim() : null);
            formOfPayment.setFopCurrencyCode(rs.getString("FOP_CURR_TYPE_CD") != null ? rs.getString("FOP_CURR_TYPE_CD").trim() : null);
            formOfPayment.setFopTypeCode(rs.getString("FOP_TYPE_CD") != null ? rs.getString("FOP_TYPE_CD").trim() : null);
            formOfPayment.setFopTypeDescription(fopTypeMap.get(formOfPayment.getFopTypeCode()));
            formOfPayments.add(formOfPayment);

            passengerDetail.setFormOfPayments(formOfPayments);

            if (isFirst) {
                isFirst = false;
                FareTaxesFees fareTaxesFees = mapCostDetailsFTF(rs);
                passengerDetail.setFareTaxesFees(fareTaxesFees);
            }
        }

        return passengerDetail;
    }

    private FareTaxesFees mapCostDetailsFTF(SqlRowSet rs) {
        String baseFareAmount;
        String baseFareCurrencyCode;

        int eqfnFareAmt = Integer.parseInt(rs.getString("EQFN_FARE_AMT"));
        if (eqfnFareAmt == 0) {
            baseFareAmount = rs.getString("FNUM_FARE_AMT");
            baseFareCurrencyCode = rs.getString("FNUM_FARE_CURR_TYPE_CD");
        } else {
            baseFareAmount = rs.getString("EQFN_FARE_AMT");
            baseFareCurrencyCode = rs.getString("EQFN_FARE_CURR_TYPE_CD");
        }

        return tuneAmountCurrency(baseFareAmount, rs.getString("FARE_TDAM_AMT"), baseFareCurrencyCode);
    }

    private FareTaxesFees tuneAmountCurrency(String baseFareAmount, String totalFareAmount, String baseFareCurrencyCode) {
        FareTaxesFees fareTaxesFees = new FareTaxesFees();

        String lastChar = baseFareCurrencyCode.substring(baseFareCurrencyCode.length() - 1);

        if (StringUtils.isNumeric(lastChar)) {
            fareTaxesFees.setBaseFareCurrencyCode(baseFareCurrencyCode.substring(0, baseFareCurrencyCode.length() - 1));

            switch (lastChar) {
                case "0":
                    fareTaxesFees.setBaseFareAmount(baseFareAmount);
                    fareTaxesFees.setTotalFareAmount(totalFareAmount);
                    break;
                case "1":
                    fareTaxesFees.setBaseFareAmount(String.valueOf(Float.parseFloat(baseFareAmount) / 10));
                    fareTaxesFees.setTotalFareAmount(String.valueOf(Float.parseFloat(totalFareAmount) / 10));
                    break;
                case "2":
                    fareTaxesFees.setBaseFareAmount(String.valueOf(Float.parseFloat(baseFareAmount) / 100));
                    fareTaxesFees.setTotalFareAmount(String.valueOf(Float.parseFloat(totalFareAmount) / 100));
                    break;
                case "3":
                    fareTaxesFees.setBaseFareAmount(String.valueOf(Float.parseFloat(baseFareAmount) / 1000));
                    fareTaxesFees.setTotalFareAmount(String.valueOf(Float.parseFloat(totalFareAmount) / 1000));
                    break;
                default:
                    fareTaxesFees.setBaseFareAmount(baseFareAmount);
                    fareTaxesFees.setTotalFareAmount(totalFareAmount);
            }
        } else {
            fareTaxesFees.setBaseFareCurrencyCode(baseFareCurrencyCode);
            fareTaxesFees.setBaseFareAmount(baseFareAmount);
            fareTaxesFees.setTotalFareAmount(totalFareAmount);
        }

        return fareTaxesFees;
    }
}
