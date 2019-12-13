package com.aa.fly.receipts.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.Ancillary;
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

    public PassengerDetail mapCostDetails(SqlRowSet rs, PassengerDetail passengerDetail) {
        List<FormOfPayment> formOfPayments = new ArrayList<>();
        Set<String> anclryDocNums = new HashSet<>();
        FareTaxesFees fareTaxesFees = null;
        int rowCount = 0;

        while (rs.next()) {

            if (rowCount == 0) {
                mapFormOfPayment(rs, formOfPayments);
                passengerDetail.setFormOfPayments(formOfPayments);
                fareTaxesFees = mapFareTaxAndFees(rs);
                passengerDetail.setFareTaxesFees(fareTaxesFees);
            } else {
                fareTaxesFees.getTaxes().add(mapTax(rs));
            }
            rowCount++;

            mapAnclry(rs, formOfPayments, anclryDocNums);
        }

        return adjustTaxesWithOtherCurrencies(sumFopAmounts(passengerDetail));
    }

    private PassengerDetail sumFopAmounts(PassengerDetail passengerDetail) {
        if (passengerDetail == null)
            return passengerDetail;

        BigDecimal passengerTotalAmount = new BigDecimal("0");

        for (int i = 0; i < passengerDetail.getFormOfPayments().size(); i++) {
            passengerTotalAmount = passengerTotalAmount.add(new BigDecimal(passengerDetail.getFormOfPayments().get(i).getFopAmount())).setScale(2, RoundingMode.CEILING);
        }

        passengerDetail.setPassengerTotalAmount(passengerTotalAmount.toString());

        return passengerDetail;
    }

    private void mapFormOfPayment(SqlRowSet rs, List<FormOfPayment> formOfPayments) {
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopAccountNumberLast4(StringUtils.isNotBlank(rs.getString("FOP_ACCT_NBR_LAST4")) ? rs.getString("FOP_ACCT_NBR_LAST4").trim() : null);
        formOfPayment.setFopIssueDate(rs.getDate("FOP_ISSUE_DT"));

        String fopAmount = StringUtils.isNotBlank(rs.getString("FOP_AMT")) ? rs.getString("FOP_AMT").trim() : null;
        String fopCurrencyCode = StringUtils.isNotBlank(rs.getString("FOP_CURR_TYPE_CD")) ? rs.getString("FOP_CURR_TYPE_CD").trim() : "";
        AmountAndCurrency fopAmountAndCurrency = new AmountAndCurrency(fopAmount, fopCurrencyCode);

        formOfPayment.setFopAmount(fopAmountAndCurrency.getAmount());
        formOfPayment.setFopCurrencyCode(fopAmountAndCurrency.getCurrencyCode());

        formOfPayment.setFopTypeCode(StringUtils.isNotBlank(rs.getString("FOP_TYPE_CD")) ? rs.getString("FOP_TYPE_CD").trim() : null);
        formOfPayment.setFopTypeDescription(fopTypeMap.get(formOfPayment.getFopTypeCode()));

        formOfPayments.add(formOfPayment);
    }

    private FormOfPayment mapAnclryFormOfPayment(SqlRowSet rs, List<FormOfPayment> formOfPayments) {
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopAccountNumberLast4(StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_ACCT_NBR_LAST4")) ? rs.getString("ANCLRY_FOP_ACCT_NBR_LAST4").trim() : null);
        formOfPayment.setFopIssueDate(rs.getDate("ANCLRY_ISSUE_DT"));

        String fopAmount = StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_AMT")) ? rs.getString("ANCLRY_FOP_AMT").trim() : null;
        String fopCurrencyCode = StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_CURR_TYPE_CD")) ? rs.getString("ANCLRY_FOP_CURR_TYPE_CD").trim() : "";
        AmountAndCurrency fopAmountAndCurrency = new AmountAndCurrency(fopAmount, fopCurrencyCode);

        formOfPayment.setFopAmount(fopAmountAndCurrency.getAmount());
        formOfPayment.setFopCurrencyCode(fopAmountAndCurrency.getCurrencyCode());

        formOfPayment.setFopTypeCode(StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_TYPE_CD")) ? rs.getString("ANCLRY_FOP_TYPE_CD").trim() : null);
        formOfPayment.setFopTypeDescription(fopTypeMap.get(formOfPayment.getFopTypeCode()));

        return formOfPayment;
    }

    private void mapAnclry(SqlRowSet rs, List<FormOfPayment> formOfPayments, Set<String> anclryDocNums) {
        FormOfPayment formOfPayment = null;
        Ancillary ancillary = null;

        String anclryDocNbr = rs.getString("ANCLRY_DOC_NBR");

        if (StringUtils.isNotBlank(anclryDocNbr) && !anclryDocNums.contains(anclryDocNbr)) {
            formOfPayment = mapAnclryFormOfPayment(rs, formOfPayments);

            ancillary = new Ancillary();
            ancillary.setAnclryDocNbr(anclryDocNbr);
            ancillary.setAnclryIssueDate(StringUtils.isNotBlank(rs.getString("ANCLRY_ISSUE_DT")) ? rs.getString("ANCLRY_ISSUE_DT").trim() : null);
            ancillary.setAnclryProdCode(StringUtils.isNotBlank(rs.getString("ANCLRY_PROD_CD")) ? rs.getString("ANCLRY_PROD_CD").trim() : null);
            String anclryProdName = StringUtils.isNotBlank(rs.getString("ANCLRY_PROD_NM")) ? rs.getString("ANCLRY_PROD_NM").trim() : "???";
            String segDeptArprtCd = StringUtils.isNotBlank(rs.getString("SEG_DEPT_ARPRT_CD")) ? rs.getString("SEG_DEPT_ARPRT_CD").trim() : null;
            String segArvlArprtCd = StringUtils.isNotBlank(rs.getString("SEG_ARVL_ARPRT_CD")) ? rs.getString("SEG_ARVL_ARPRT_CD").trim() : null;

            if (anclryProdName != null) {
                ancillary.setAnclryProdName(anclryProdName + " (" + segDeptArprtCd + " - " + segArvlArprtCd + ")");
            }

            String anclryPriceCurrencyAmount = StringUtils.isNotBlank(rs.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")) ? rs.getString("ANCLRY_PRICE_LCL_CURNCY_AMT").trim() : null;
            ancillary.setAnclryPriceCurrencyAmount(StringUtils.isNotBlank(rs.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")) ? rs.getString("ANCLRY_PRICE_LCL_CURNCY_AMT").trim() : null);

            ancillary.setAnclryPriceCurrencyCode(StringUtils.isNotBlank(rs.getString("ANCLRY_PRICE_LCL_CURNCY_CD")) ? rs.getString("ANCLRY_PRICE_LCL_CURNCY_CD").trim() : null);

            String anclrySalesCurrencyAmount = StringUtils.isNotBlank(rs.getString("ANCLRY_SLS_CURNCY_AMT")) ? rs.getString("ANCLRY_SLS_CURNCY_AMT").trim() : null;
            ancillary.setAnclrySalesCurrencyAmount(anclrySalesCurrencyAmount);

            ancillary.setAnclrySalesCurrencyCode(StringUtils.isNotBlank(rs.getString("ANCLRY_SLS_CURNCY_CD")) ? rs.getString("ANCLRY_SLS_CURNCY_CD").trim() : null);

            BigDecimal anclryTaxCurrencyAmount = new BigDecimal(anclrySalesCurrencyAmount).subtract(new BigDecimal(anclryPriceCurrencyAmount)).setScale(2, RoundingMode.CEILING);

            ancillary.setAnclryTaxCurrencyAmount(anclryTaxCurrencyAmount.toString());

            anclryDocNums.add(anclryDocNbr);

            formOfPayment.getAncillaries().add(ancillary);
            formOfPayments.add(formOfPayment);
        }
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

    public PassengerDetail adjustTaxesWithOtherCurrencies(PassengerDetail passengerDetail) {
        if (passengerDetail == null || passengerDetail.getFareTaxesFees() == null)
            return passengerDetail;

        FareTaxesFees fareTaxesFees = passengerDetail.getFareTaxesFees();
        String baseFareCurrencyCode = fareTaxesFees.getBaseFareCurrencyCode();
        BigDecimal totalFareAmount = new BigDecimal(fareTaxesFees.getTotalFareAmount());
        BigDecimal baseFareAmount = new BigDecimal(fareTaxesFees.getBaseFareAmount());
        BigDecimal totalTaxAmount = totalFareAmount.subtract(baseFareAmount);
        fareTaxesFees.setTaxFareAmount(totalTaxAmount.toString());

        Set<Tax> taxes = passengerDetail.getFareTaxesFees().getTaxes();

        long count = taxes.stream().filter(t -> !baseFareCurrencyCode.equals(t.getTaxCurrencyCode())).count();

        if (count > 0) {
            double baseFareCurencyTaxAmoutDouble = taxes.stream().filter(t -> baseFareCurrencyCode.equals(t.getTaxCurrencyCode())).mapToDouble(t -> Double.valueOf(t.getTaxAmount())).sum();
            BigDecimal baseFareCurrencyTax = BigDecimal.valueOf(baseFareCurencyTaxAmoutDouble);
            String taxAmount = (totalTaxAmount.subtract(baseFareCurrencyTax)).divide(new BigDecimal(count)).setScale(2, RoundingMode.CEILING).toString();
            taxes.stream().filter(t -> !baseFareCurrencyCode.equals(t.getTaxCurrencyCode())).forEach(t -> {
                t.setTaxAmount(taxAmount);
                t.setTaxCurrencyCode(baseFareCurrencyCode);
            });
        }

        return passengerDetail;
    }
}
