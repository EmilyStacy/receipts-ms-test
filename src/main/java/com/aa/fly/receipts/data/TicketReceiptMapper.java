package com.aa.fly.receipts.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.Tax;
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
        FareTaxesFees fareTaxesFees = null;
        int rowCount = 0;

        while (rs.next()) {

            if (rowCount == 0) {
                FormOfPayment formOfPayment = new FormOfPayment();
                formOfPayment.setFopAccountNumberLast4(rs.getString("FOP_ACCT_NBR_LAST4") != null ? rs.getString("FOP_ACCT_NBR_LAST4").trim() : null);
                formOfPayment.setFopIssueDate(rs.getDate("FOP_ISSUE_DT"));

                AmountAndCurrency fopAmountAndCurrency = new AmountAndCurrency( rs.getString("FOP_AMT") != null ? rs.getString("FOP_AMT").trim() : null,  rs.getString("FOP_CURR_TYPE_CD") != null ? rs.getString("FOP_CURR_TYPE_CD").trim() : "" );
                formOfPayment.setFopAmount(fopAmountAndCurrency.getAmount());
                formOfPayment.setFopCurrencyCode(fopAmountAndCurrency.getCurrencyCode());

                formOfPayment.setFopTypeCode(rs.getString("FOP_TYPE_CD") != null ? rs.getString("FOP_TYPE_CD").trim() : null);
                formOfPayment.setFopTypeDescription(fopTypeMap.get(formOfPayment.getFopTypeCode()));
                formOfPayments.add(formOfPayment);

                passengerDetail.setFormOfPayments(formOfPayments);
                fareTaxesFees = mapFareTaxAndFees(rs);
                passengerDetail.setFareTaxesFees(fareTaxesFees);
            } else {
                fareTaxesFees.getTaxes().add(mapTax(rs));
            }
            rowCount++;
        }

        return passengerDetail;
    }

    private FareTaxesFees mapFareTaxAndFees(SqlRowSet rs) {

        FareTaxesFees fareTaxesFees = new FareTaxesFees();

        String baseFareAmount;
        String baseFareCurrencyCode;

        String totalFareAmount = rs.getString("FARE_TDAM_AMT");

        int eqfnFareAmt = Integer.parseInt(rs.getString("EQFN_FARE_AMT"));
        if (eqfnFareAmt == 0) {
            baseFareAmount = rs.getString("FNUM_FARE_AMT");
            baseFareCurrencyCode = rs.getString("FNUM_FARE_CURR_TYPE_CD");
        } else {
            baseFareAmount = rs.getString("EQFN_FARE_AMT");
            baseFareCurrencyCode = rs.getString("EQFN_FARE_CURR_TYPE_CD");
        }

        AmountAndCurrency baseFareAmountAndCurrency = new AmountAndCurrency(baseFareAmount, baseFareCurrencyCode);
        AmountAndCurrency totalFareAmountAndCurrency = new AmountAndCurrency(totalFareAmount, baseFareCurrencyCode);

        fareTaxesFees.setBaseFareCurrencyCode(baseFareAmountAndCurrency.getCurrencyCode());
        fareTaxesFees.setBaseFareAmount(baseFareAmountAndCurrency.getAmount());
        fareTaxesFees.setTotalFareAmount(totalFareAmountAndCurrency.getAmount());
        fareTaxesFees.getTaxes().add(mapTax(rs));
        return fareTaxesFees;

    }

    private Tax mapTax(SqlRowSet rs) {
        Tax tax = new Tax();
        tax.setTaxCodeSequenceId(rs.getString("TAX_CD_SEQ_ID"));
        tax.setTaxCode(rs.getString("TAX_CD"));
        AmountAndCurrency amountAndCurrency = new AmountAndCurrency(rs.getString("TAX_AMT"), rs.getString("TAX_CURR_TYPE_CD"));
        tax.setTaxAmount(amountAndCurrency.getAmount());
        tax.setTaxCurrencyCode(amountAndCurrency.getCurrencyCode());
        return tax;
    }

}
