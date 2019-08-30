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

public class FindTicketReceiptDetailOneWayNoConnectionSteps extends SpringIntegrationTest {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find my ticket receipt details for scenario \"(.+)\"$")
    public void the_client_want_to_find_ticket_summary(String scenario) {

    }

    @When("^I search with one-way with no connection ticket number \"(.+)\", last name \"(.+)\", first name \"(.+)\", departure date \"(.+)\"$")
    public void the_client_enters_ticket_number(String ticketNumber, String lastName, String firstName, String departureDate) {
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

    @Then("^I get a successful response with segDepartureDate \"([^\"]*)\", segAirportFrom \"([^\"]*)\", segAirportTo \"([^\"]*)\", segTimeFrom \"([^\"]*)\", segTimeTo \"([^\"]*)\", flightNum \"([^\"]*)\", bookingClass \"([^\"]*)\", and fareBasis \"([^\"]*)\"$")
    public void i_submit_my_request(String segDepartureDate, String segAirportFrom, String segAirportTo, String segTimeFrom, String segTimeTo, String flightNum, String bookingClass, String fareBasis)
            throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        SegmentDetail segmentDetail = ticketReceipt.getSegmentDetails().get(0);

        Assert.assertEquals(segDepartureDate, segmentDetail.getSegmentDepartureDate());
        Assert.assertEquals(segAirportFrom, segmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(segAirportTo, segmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(segTimeFrom, segmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(segTimeTo, segmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(flightNum, segmentDetail.getFlightNumber());
        Assert.assertEquals(bookingClass, segmentDetail.getBookingClass());
        Assert.assertEquals(fareBasis, segmentDetail.getFareBasis());
    }
}
