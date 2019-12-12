package com.aa.fly.receipts.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.config.AppConfig;
import com.aa.fly.receipts.domain.Airport;
import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.Tax;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.AirportService;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketReceiptMapperTest {
    @Mock
    private SqlRowSet resultSet;

    @Mock
    private AirportService airportService;

    @InjectMocks
    private TicketReceiptMapper ticketReceiptMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void mapResultSet() throws ParseException {

        Mockito.when(resultSet.next()).thenReturn(true, false); // first time return true and second time return false
        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn("001");
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        final Date departureDate = dateFormat.parse("2019-09-30");
        final Date arrivalDate = dateFormat.parse("2019-09-30");
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(departureDate.getTime()));

        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn("MCO");
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn("MIA");
        Mockito.when(airportService.getAirport("MCO")).thenReturn(getAirport("MCO", "Orlando International", "Orlando", "FL", "USA", "United States"));
        Mockito.when(airportService.getAirport("MIA")).thenReturn(getAirport("MIA", "Miami International", "Miami", "FL", "USA", "United States"));
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");
        Mockito.when(resultSet.getString("AADVANT_NBR")).thenReturn("279RFY4");

        // mock passenger details
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("AADVANT_NBR")).thenReturn("279RFY4");
        Mockito.when(resultSet.getString("LYLTY_OWN_CD")).thenReturn("AA  ");

        // mock segment details
        Mockito.when(resultSet.getDate("SEG_DEPT_DT")).thenReturn(new java.sql.Date(departureDate.getTime()));
        Mockito.when(resultSet.getDate("SEG_ARVL_DT")).thenReturn(new java.sql.Date(arrivalDate.getTime()));
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn("MCO");
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn("MIA");
        Mockito.when(resultSet.getString("SEG_OPERAT_CARRIER_CD")).thenReturn("AA");
        final String segmentDepartureTime = "8:05:00";
        Mockito.when(resultSet.getString("SEG_DEPT_TM")).thenReturn(segmentDepartureTime);
        final String segmentArrivalTime = "9:05:00";
        Mockito.when(resultSet.getString("SEG_ARVL_TM")).thenReturn(segmentArrivalTime);
        Mockito.when(resultSet.getString("FLIGHT_NBR")).thenReturn("1536");
        Mockito.when(resultSet.getString("BOOKING_CLASS")).thenReturn("B");
        Mockito.when(resultSet.getString("FARE_BASE")).thenReturn("QVAJZNB3");
        Mockito.when(resultSet.getString("COUPON_SEQ_NBR")).thenReturn("1");

        TicketReceipt item = ticketReceiptMapper.mapTicketReceipt(resultSet);

        // header
        assertThat(item.getAirlineAccountCode()).isEqualTo("001");
        assertThat(item.getTicketIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(departureDate);
        assertThat(item.getOriginAirport().getCode()).isEqualTo("MCO");
        assertThat(item.getDestinationAirport().getCode()).isEqualTo("MIA");
        assertThat(item.getOriginAirport().getName()).isEqualTo("Orlando International");
        assertThat(item.getDestinationAirport().getName()).isEqualTo("Miami International");
        assertThat(item.getPnr()).isEqualTo("MRYMPT");

        // passenger details
        assertThat(item.getPassengerDetails().size()).isEqualTo(1);
        PassengerDetail passengerDetail = item.getPassengerDetails().get(0);
        assertThat(passengerDetail.getTicketNumber()).isEqualTo("2335038507");
        assertThat(passengerDetail.getFirstName()).isEqualTo("SIMON");
        assertThat(passengerDetail.getLastName()).isEqualTo("TEST");
        assertThat(passengerDetail.getAdvantageNumber()).isEqualTo("279RFY4");
        assertThat(passengerDetail.getLoyaltyOwnerCode()).isEqualTo("AA");

        // segment details
        assertThat(item.getSegmentDetails().size()).isEqualTo(1);
        SegmentDetail segmentDetail = item.getSegmentDetails().get(0);
        assertThat(segmentDetail.getDepartureAirport().getName()).isEqualTo("Orlando International");
        assertThat(segmentDetail.getArrivalAirport().getName()).isEqualTo("Miami International");
        assertThat(segmentDetail.getSegmentDepartureDate()).isEqualTo(departureDate);
        assertThat(segmentDetail.getSegmentArrivalDate()).isEqualTo(arrivalDate);
        assertThat(segmentDetail.getDepartureAirport().getCode()).isEqualTo("MCO");
        assertThat(segmentDetail.getArrivalAirport().getCode()).isEqualTo("MIA");

        assertThat(segmentDetail.getSegmentDepartureTime()).isEqualTo(segmentDepartureTime);
        assertThat(segmentDetail.getSegmentArrivalTime()).isEqualTo(segmentArrivalTime);
        assertThat(segmentDetail.getFlightNumber()).isEqualTo("1536");
        assertThat(segmentDetail.getCarrierCode()).isEqualTo("AA");
        assertThat(segmentDetail.getBookingClass()).isEqualTo("B");
        assertThat(segmentDetail.getFareBasis()).isEqualTo("QVAJZNB3");
        assertThat(segmentDetail.getReturnTrip()).isEqualTo("false");
    }

    @Test
    public void mapResultSetWithNulls() throws ParseException {

        Mockito.when(resultSet.next()).thenReturn(true, false); // first time return true and second time return false
        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-09-30").getTime()));

        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn(null);
        Mockito.when(airportService.getAirport(null)).thenReturn(null);
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");
        Mockito.when(resultSet.getString("LYLTY_OWN_CD")).thenReturn(null);

        TicketReceipt item = ticketReceiptMapper.mapTicketReceipt(resultSet);
        assertThat(item.getAirlineAccountCode()).isNull();
        assertThat(item.getTicketIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(dateFormat.parse("2019-09-30"));
        assertThat(item.getOriginAirport()).isNull();
        assertThat(item.getDestinationAirport()).isNull();
        assertThat(item.getPnr()).isEqualTo("MRYMPT");
    }

    @Test
    public void testMapCostDetails() throws ParseException {
        PassengerDetail passengerDetail = new PassengerDetail();

        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString("FOP_ACCT_NBR_LAST4")).thenReturn("0006");
        Mockito.when(resultSet.getDate("FOP_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        Mockito.when(resultSet.getString("FOP_AMT")).thenReturn("225295");
        Mockito.when(resultSet.getString("FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("FOP_TYPE_CD")).thenReturn("CCBA");

        Mockito.when(resultSet.getString("FNUM_FARE_AMT")).thenReturn("77674");
        Mockito.when(resultSet.getString("FNUM_FARE_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("EQFN_FARE_AMT")).thenReturn("0");
        Mockito.when(resultSet.getString("EQFN_FARE_CURR_TYPE_CD")).thenReturn("");
        Mockito.when(resultSet.getString("FARE_TDAM_AMT")).thenReturn("84930");

        Mockito.when(resultSet.getString("TAX_CD_SEQ_ID")).thenReturn("1");
        Mockito.when(resultSet.getString("TAX_CD")).thenReturn("XA");
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("450");
        Mockito.when(resultSet.getString("TAX_CURR_TYPE_CD")).thenReturn("USD2");

        Mockito.when(resultSet.getString("ANCLRY_DOC_NBR")).thenReturn("654200213");
        Mockito.when(resultSet.getString("ANCLRY_ISSUE_DT")).thenReturn("2019-11-07");
        Mockito.when(resultSet.getString("ANCLRY_PROD_CD")).thenReturn("090");
        Mockito.when(resultSet.getString("ANCLRY_PROD_NM")).thenReturn("MAIN CABIN EXTRA");
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn("DFW");
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn("BDL");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")).thenReturn("72.91");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_AMT")).thenReturn("78.38");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_CD")).thenReturn("USD");

        Mockito.when(resultSet.getString("ANCLRY_FOP_ACCT_NBR_LAST4")).thenReturn("1111");
        Mockito.when(resultSet.getString("ANCLRY_FOP_AMT")).thenReturn("53628");
        Mockito.when(resultSet.getString("ANCLRY_FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("ANCLRY_FOP_TYPE_CD")).thenReturn("CCBA");

        Ancillary ancillary = new Ancillary("654200213", "2019-11-07", "090", "MAIN CABIN EXTRA (DFW - BDL)", "72.91", "USD", "78.38", "USD", "5.47");

        ticketReceiptMapper.setFopTypeMap(new AppConfig().fopTypeMap());
        passengerDetail = ticketReceiptMapper.mapCostDetails(resultSet, passengerDetail);
        List<FormOfPayment> fops = passengerDetail.getFormOfPayments();

        assertThat(fops.size()).isEqualTo(2);
        assertThat(fops.get(0).getFopAccountNumberLast4()).isEqualTo("0006");
        assertThat(fops.get(0).getFopIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(fops.get(0).getFopAmount()).isEqualTo("2252.95");
        assertThat(fops.get(0).getFopCurrencyCode()).isEqualTo("USD");
        assertThat(fops.get(0).getFopTypeCode()).isEqualTo("CCBA");

        assertThat(fops.get(0).getAncillaries().contains(ancillary));
    }

    @Test
    public void testAdjustTaxesWithOtherCurrencies_oneCreditCardAsFop() {
        PassengerDetail passengerDetail = new PassengerDetail();
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setTotalFareAmount("1000.00");
        fareTaxesFees.setBaseFareAmount("700");
        fareTaxesFees.setBaseFareCurrencyCode("USD");

        Tax gbTax = new Tax();
        gbTax.setTaxCode("GB");
        gbTax.setTaxAmount("200");
        gbTax.setTaxCodeSequenceId("1");
        gbTax.setTaxCurrencyCode("USD");

        Tax xfTax = new Tax();
        xfTax.setTaxCode("XF");
        xfTax.setTaxAmount("75");
        xfTax.setTaxCodeSequenceId("2");
        xfTax.setTaxCurrencyCode("CAD");

        Tax xaTax = new Tax();
        xaTax.setTaxCode("XA");
        xaTax.setTaxAmount("50");
        xaTax.setTaxCodeSequenceId("3");
        xaTax.setTaxCurrencyCode("USD");

        fareTaxesFees.getTaxes().add(gbTax);
        fareTaxesFees.getTaxes().add(xfTax);
        fareTaxesFees.getTaxes().add(xaTax);

        passengerDetail.setFareTaxesFees(fareTaxesFees);

        ticketReceiptMapper.adjustTaxesWithOtherCurrencies(passengerDetail);

        Tax adjustedTax = fareTaxesFees.getTaxes().stream().filter(t -> "XF".equals(t.getTaxCode())).findFirst().orElse(null);
        assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
        assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("USD");
        assertThat(adjustedTax.getTaxAmount()).isEqualTo("50.00");
    }

    public Airport getAirport(String code, String name, String city, String state, String countryCode, String countryName) {
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
