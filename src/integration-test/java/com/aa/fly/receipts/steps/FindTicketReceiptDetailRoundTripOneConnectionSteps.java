package com.aa.fly.receipts.steps;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;

public class FindTicketReceiptDetailRoundTripOneConnectionSteps extends SpringIntegrationTest {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find round trip with one connection ticket receipt for scenario \"([^\"]*)\"$")
    public void i_want_to_find_round_trip_with_one_connection_ticket_receipt_for_scenario(String arg1) throws Throwable {
    }

    @When("^I search for round trip with one connection ticket number \"([^\"]*)\", last name \"([^\"]*)\", first name \"([^\"]*)\", departure date \"([^\"]*)\"$")
    public void i_search_for_round_trip_with_one_connection_ticket_number_last_name_first_name_departure_date(String ticketNumber, String lastName, String firstName, String departureDate)  {
        String branchApplicationUrl = System.getProperty("branch.application.url");

        criteria.setTicketNumber(ticketNumber);
        criteria.setLastName(lastName);
        criteria.setFirstName(firstName);
        try {
            criteria.setDepartureDate(dateFormat.parse(departureDate));
        } catch (ParseException e) {
        }
        executePost(branchApplicationUrl + "/api/ticket-receipt", criteria);
    }

    @Then("^I get a successful response for round trip with one connection ticket receipt with departSeg1DepartureDate \"([^\"]*)\", departSeg1AirportFrom \"([^\"]*)\", departSeg1AirportTo \"([^\"]*)\", "
            + "departSeg1TimeFrom \"([^\"]*)\", departSeg1TimeTo \"([^\"]*)\", departSeg1FlightNum \"([^\"]*)\", departSeg1_isReturnTrip \"([^\"]*)\", departSeg2DepartureDate \"([^\"]*)\", "
            + "departSeg2AirportFrom \"([^\"]*)\", departSeg2AirportTo \"([^\"]*)\", departSeg2TimeFrom \"([^\"]*)\", departSeg2TimeTo \"([^\"]*)\", departSeg2FlightNum \"([^\"]*)\", departSeg2ReturnTrip \"([^\"]*)\", "
            + "returnSeg1DepartureDate \"([^\"]*)\", returnSeg1AirportFrom \"([^\"]*)\", returnSeg1AirportTo \"([^\"]*)\", "
            + "returnSeg1TimeFrom \"([^\"]*)\", returnSeg1TimeTo \"([^\"]*)\", returnSeg1FlightNum \"([^\"]*)\", returnSeg1ReturnTrip \"([^\"]*)\", returnSeg2DepartureDate \"([^\"]*)\", "
            + "returnSeg2AirportFrom \"([^\"]*)\", returnSeg2AirportTo \"([^\"]*)\", returnSeg2TimeFrom \"([^\"]*)\", returnSeg2TimeTo \"([^\"]*)\", returnSeg2FlightNum \"([^\"]*)\"$")
    public void i_get_a_successful_response_for_round_trip_with_one_connection_ticket_receipt_with_departSeg_DepartureDate_departSeg_AirportFrom_departSeg_AirportTo_departSeg_TimeFrom_departSeg_TimeTo_departSeg_FlightNum_departSeg__isReturnTrip_departSeg_DepartureDate_departSeg_AirportFrom_departSeg_AirportTo_departSeg_TimeFrom_departSeg_TimeTo_departSeg_FlightNum_departSeg_ReturnTrip(
            String departSeg1DepartureDate, String departSeg1AirportFrom, String departSeg1AirportTo, String departSeg1TimeFrom,String departSeg1TimeTo, String departSeg1FlightNum,String departSeg1_isReturnTrip,
            String departSeg2DepartureDate, String departSeg2AirportFrom, String departSeg2AirportTo, String departSeg2TimeFrom, String departSeg2TimeTo, String departSeg2FlightNum, String departSeg2ReturnTrip,
            String returnSeg1DepartureDate, String returnSeg1AirportFrom,String returnSeg1AirportTo, String returnSeg1TimeFrom,String returnSeg1TimeTo, String returnSeg1FlightNum,String returnSeg1ReturnTrip,
            String returnSeg2DepartureDate, String returnSeg2AirportFrom, String returnSeg2AirportTo, String returnSeg2TimeFrom, String returnSeg2TimeTo, String returnSeg2FlightNum) {

        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        SegmentDetail firstDepartureSegmentDetail = ticketReceipt.getSegmentDetails().get(0);
        SegmentDetail secondDepartureSegmentDetail = ticketReceipt.getSegmentDetails().get(1);
        SegmentDetail firstReturnSegmentDetail = ticketReceipt.getSegmentDetails().get(2);
        SegmentDetail secondReturnSegmentDetail = ticketReceipt.getSegmentDetails().get(3);

        Assert.assertEquals(departSeg1DepartureDate, dateFormat.format(firstDepartureSegmentDetail.getSegmentDepartureDate()));
        Assert.assertEquals(departSeg1AirportFrom, firstDepartureSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(departSeg1AirportTo, firstDepartureSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(departSeg1TimeFrom, firstDepartureSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(departSeg1TimeTo, firstDepartureSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(departSeg1FlightNum, firstDepartureSegmentDetail.getFlightNumber());
        Assert.assertEquals(departSeg1_isReturnTrip, firstDepartureSegmentDetail.getReturnTrip());

        Assert.assertEquals(departSeg2DepartureDate, dateFormat.format(secondDepartureSegmentDetail.getSegmentDepartureDate()));
        Assert.assertEquals(departSeg2AirportFrom, secondDepartureSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(departSeg2AirportTo, secondDepartureSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(departSeg2TimeFrom, secondDepartureSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(departSeg2TimeTo, secondDepartureSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(departSeg2FlightNum, secondDepartureSegmentDetail.getFlightNumber());
        Assert.assertEquals(departSeg2ReturnTrip, secondDepartureSegmentDetail.getReturnTrip());


        Assert.assertEquals(returnSeg1DepartureDate, dateFormat.format(firstReturnSegmentDetail.getSegmentDepartureDate()));
        Assert.assertEquals(returnSeg1AirportFrom, firstReturnSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(returnSeg1AirportTo, firstReturnSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(returnSeg1TimeFrom, firstReturnSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(returnSeg1TimeTo, firstReturnSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(returnSeg1FlightNum, firstReturnSegmentDetail.getFlightNumber());
        Assert.assertEquals(returnSeg1ReturnTrip, firstReturnSegmentDetail.getReturnTrip());

        Assert.assertEquals(returnSeg2DepartureDate, dateFormat.format(secondReturnSegmentDetail.getSegmentDepartureDate()));
        Assert.assertEquals(returnSeg2AirportFrom, secondReturnSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(returnSeg2AirportTo, secondReturnSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(returnSeg2TimeFrom, secondReturnSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(returnSeg2TimeTo, secondReturnSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(returnSeg2FlightNum, secondReturnSegmentDetail.getFlightNumber());

    }
}
