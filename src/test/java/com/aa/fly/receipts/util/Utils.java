package com.aa.fly.receipts.util;

import com.aa.fly.receipts.domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static TicketReceiptRsRow mockTicketReceiptRsRow() throws ParseException {
        return TicketReceiptRsRow.builder()

                .airlnAcctCd(Constants.AIRLN_ACCT_CD)
                .ticketIssueDt(Constants.dateFormat.parse(Constants.TICKET_ISSUE_DT))
                .depDt(Constants.dateFormat.parse(Constants.DEP_DT))
                .pnr(Constants.PNR)
                .orgAtoCd(Constants.ORG_ATO_CD)
                .destAtoCd(Constants.DEST_ATO_CD)
                .ticketNbr(Constants.TICKET_NBR)
                .firstNm(Constants.FIRST_NM)
                .lastNm(Constants.LAST_NM)
                .aadvantNbr(Constants.AADVANT_NBR)
                .lyltyOwnCd(Constants.LYLTY_OWN_CD)
                .segDeptDt(Constants.dateFormat.parse(Constants.SEG_DEPT_DT))
                .segDeptTm(Constants.SEG_DEPT_TM)
                .segDeptArprtCd(Constants.SEG_DEPT_ARPRT_CD)
                .segArvltDt(Constants.dateFormat.parse(Constants.SEG_ARVL_DT))
                .segArvlTm(Constants.SEG_ARVL_TM)
                .segArvlArprtCd(Constants.SEG_ARVL_ARPRT_CD)
                .segCouponStatusCd(Constants.SEG_COUPON_STATUS_CD)
                .segOperatCarrierCd(Constants.SEG_OPERAT_CARRIER_CD)
                .flightNbr(Constants.FLIGHT_NBR)
                .bookingClass(Constants.BOOKING_CLASS)
                .fareBase(Constants.FARE_BASE)
                .couponSeqNbr(Constants.COUPON_SEQ_NBR)

                .eqfnFareAmt(Constants.EQFN_FARE_AMOUNT)
                .eqfnFareCurrTypeCd(Constants.EQFN_FARE_CURRENCY_CODE)
                .fnumFareAmt(Constants.FNUM_FARE_AMOUNT)
                .fnumFareCurrTypeCd(Constants.FNUM_FARE_CURRENCY_CODE)
                .fareTdamAmt(Constants.FARE_TDAM_AMOUNT)
                .fopIssueDt(Constants.dateFormat.parse(Constants.FOP_ISSUE_DATE))
                .fopTypeCd(Constants.FOP_TYPE_CODE)
                .fopAcctNbrLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4)
                .fopAmt(Constants.FOP_AMOUNT)
                .fopCurrTypeCd(Constants.FOP_CURRENCY_CODE)
                .fopSeqId(Constants.FOP_SEQ_ID)
                .anclryIssueDt(Constants.dateFormat.parse(Constants.ANCLRY_ISSUE_DATE))
                .anclryFopAcctNbrLast4(Constants.ANCLRY_FOP_ACCOUNT_NUMBER_LAST4)
                .anclryFopAmt(Constants.ANCLRY_FOP_AMOUNT)
                .anclryFopCurrTypeCd(Constants.ANCLRY_FOP_CURRENCY_CODE)
                .anclryFopTypeCd(Constants.ANCLRY_FOP_TYPE_CODE)

                .build();
    }

    public static void addAncillaryToTicketReceiptRow(final TicketReceiptRsRow ticketReceiptRsRow) throws ParseException {
        ticketReceiptRsRow.setAnclryDocNbr(Constants.ANCLRY_DOC_NBR);
        ticketReceiptRsRow.setAnclryIssueDt(dateFormat.parse(Constants.ANCLRY_ISSUE_DATE));
        ticketReceiptRsRow.setAnclryProdNm(Constants.ANCLRY_PROD_NAME);
        ticketReceiptRsRow.setAnclryProdCd(Constants.ANCLRY_PROD_CODE);
        ticketReceiptRsRow.setAnclryPriceLclCurncyAmt(Constants.ANCLRY_PRICE_CURRENCY_AMOUNT);
        ticketReceiptRsRow.setAnclryPriceLclCurncyCd(Constants.ANCLRY_PRICE_CURRENCY_CODE);
        ticketReceiptRsRow.setAnclrySlsCurncyAmt(Constants.ANCLRY_SALES_CURRENCY_AMOUNT);
        ticketReceiptRsRow.setAnclrySlsCurncyCd(Constants.ANCLRY_SALES_CURRENCY_CODE);
    }

    public static TicketReceipt mockTicketReceipt() throws ParseException {
        TicketReceipt ticketReceiptMock = new TicketReceipt();
        ticketReceiptMock.setAirlineAccountCode(Constants.AIRLN_ACCT_CD);
        ticketReceiptMock.setTicketIssueDate(Constants.dateFormat.parse(Constants.TICKET_ISSUE_DT));
        ticketReceiptMock.setDepartureDate(Constants.dateFormat.parse(Constants.DEP_DT));
        ticketReceiptMock.setPnr(Constants.PNR);
        ticketReceiptMock.setOriginAirport(getAirport(Constants.SEG_DEPT_ARPRT_CD, Constants.SEG_DEPT_ARPRT_NAME, Constants.SEG_DEPT_ARPRT_CITY, Constants.SEG_DEPT_ARPRT_STATE, Constants.SEG_DEPT_ARPRT_COUNTRY_CD, Constants.SEG_DEPT_ARPRT_COUNTRY_NM));
        ticketReceiptMock.setDestinationAirport(getAirport(Constants.SEG_ARVL_ARPRT_CD, Constants.SEG_ARVL_ARPRT_NAME, Constants.SEG_ARVL_ARPRT_CITY, Constants.SEG_ARVL_ARPRT_STATE, Constants.SEG_ARVL_ARPRT_COUNTRY_CD, Constants.SEG_ARVL_ARPRT_COUNTRY_NM));

        PassengerDetail passengerDetail = new PassengerDetail();
        passengerDetail.setTicketNumber(Constants.TICKET_NBR);
        passengerDetail.setFirstName(Constants.FIRST_NM);
        passengerDetail.setLastName(Constants.LAST_NM);
        passengerDetail.setAdvantageNumber(Constants.AADVANT_NBR);
        passengerDetail.setLoyaltyOwnerCode(Constants.LYLTY_OWN_CD);

        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setBaseFareAmount(Constants.BASE_FARE_AMOUNT);
        fareTaxesFees.setBaseFareCurrencyCode(Constants.BASE_FARE_CURRENCY_CODE);
        fareTaxesFees.setTotalFareAmount(Constants.TOTAL_FARE_AMOUNT);
        fareTaxesFees.setTaxFareAmount(Constants.TAX_FARE_AMOUNT);

        addOneTaxItem(fareTaxesFees);

        passengerDetail.setFareTaxesFees(fareTaxesFees);

        addOneFormOfPayment(passengerDetail);

        ticketReceiptMock.getPassengerDetails().add(passengerDetail);

        addOneSegment(ticketReceiptMock);

        return ticketReceiptMock;
    }

    public static void addOneFormOfPayment(PassengerDetail passengerDetail) throws ParseException {
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(Constants.dateFormat.parse(Constants.FOP_ISSUE_DATE));
        formOfPayment.setFopTypeCode(Constants.FOP_TYPE_CODE);
        formOfPayment.setFopTypeDescription(Constants.FOP_TYPE_DESCRIPTION);
        formOfPayment.setFopAccountNumberLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4);
        formOfPayment.setFopAmount(Constants.FOP_AMOUNT);
        formOfPayment.setFopCurrencyCode(Constants.FOP_CURRENCY_CODE);

        List<FormOfPayment> formOfPayments = new ArrayList<>();
        formOfPayments.add(formOfPayment);

        passengerDetail.setFormOfPayments(formOfPayments);
    }

    public static void addOneAncillary(FormOfPayment formOfPayment) throws ParseException {
        Ancillary ancillary = new Ancillary();
        ancillary.setAnclryDocNbr(Constants.ANCLRY_DOC_NBR);
        ancillary.setAnclryIssueDate(Constants.ANCLRY_ISSUE_DATE);
        ancillary.setAnclryProdCode(Constants.ANCLRY_PROD_CODE);
        ancillary.setAnclryProdName(Constants.ANCLRY_PROD_NAME);
        ancillary.setAnclryPriceCurrencyAmount(Constants.ANCLRY_PRICE_CURRENCY_AMOUNT);
        ancillary.setAnclryPriceCurrencyCode(Constants.ANCLRY_PRICE_CURRENCY_CODE);
        ancillary.setAnclrySalesCurrencyAmount(Constants.ANCLRY_SALES_CURRENCY_AMOUNT);
        ancillary.setAnclrySalesCurrencyCode(Constants.ANCLRY_SALES_CURRENCY_CODE);
        ancillary.setAnclryTaxCurrencyAmount(Constants.ANCLRY_TAX_CURRENCY_AMOUNT);

        Set<Ancillary> ancillaries = formOfPayment.getAncillaries();
        ancillaries.add(ancillary);

        formOfPayment.setAncillaries(ancillaries);
    }

    public static void addOneTaxItem(FareTaxesFees fareTaxesFees) throws ParseException {
        Tax tax = new Tax();
        tax.setTaxCodeSequenceId(Constants.TAX_CODE_SEQUENCE_ID);
        tax.setTaxCode(Constants.TAX_CODE);
        tax.setTaxDescription(Constants.TAX_DESCRIPTION);
        tax.setCityCode(Constants.CITY_CODE);
        tax.setTaxAmount(Constants.TAX_AMOUNT);
        tax.setTaxCurrencyCode(Constants.TAX_CURRENCY_CODE);

        fareTaxesFees.getTaxes().add(tax);
    }

    public static void addOneSegment(TicketReceipt ticketReceiptMock) throws ParseException {
        SegmentDetail segmentDetail = new SegmentDetail();
        segmentDetail.setSegmentDepartureDate(Constants.dateFormat.parse(Constants.SEG_DEPT_DT));
        segmentDetail.setSegmentDepartureTime(Constants.SEG_DEPT_TM);
        segmentDetail.setDepartureAirport(getAirport(Constants.SEG_DEPT_ARPRT_CD, Constants.SEG_DEPT_ARPRT_NAME, Constants.SEG_DEPT_ARPRT_CITY, Constants.SEG_DEPT_ARPRT_STATE, Constants.SEG_DEPT_ARPRT_COUNTRY_CD, Constants.SEG_DEPT_ARPRT_COUNTRY_NM));
        segmentDetail.setSegmentArrivalDate(Constants.dateFormat.parse(Constants.SEG_ARVL_DT));
        segmentDetail.setSegmentArrivalTime(Constants.SEG_ARVL_TM);
        segmentDetail.setArrivalAirport(getAirport(Constants.SEG_ARVL_ARPRT_CD, Constants.SEG_ARVL_ARPRT_NAME, Constants.SEG_ARVL_ARPRT_CITY, Constants.SEG_ARVL_ARPRT_STATE, Constants.SEG_ARVL_ARPRT_COUNTRY_CD, Constants.SEG_ARVL_ARPRT_COUNTRY_NM));
        segmentDetail.setSegmentStatus(Constants.SEG_COUPON_STATUS_CD);
        segmentDetail.setCarrierCode(Constants.SEG_OPERAT_CARRIER_CD);
        segmentDetail.setFlightNumber(Constants.FLIGHT_NBR);
        segmentDetail.setBookingClass(Constants.BOOKING_CLASS);
        segmentDetail.setFareBasis(Constants.FARE_BASE);
        segmentDetail.setReturnTrip("false");

        ticketReceiptMock.getSegmentDetails().add(segmentDetail);
    }

    public static Airport getAirport(String code, String name, String city, String state, String countryCode, String countryName) {
        Airport airport = new Airport();
        airport.setCode(code);
        airport.setCity(city);
        airport.setCountryCode(countryCode);
        airport.setCountryName(countryName);
        airport.setName(name);
        airport.setStateCode(state);
        return airport;
    }

}
