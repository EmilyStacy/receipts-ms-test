package com.aa.fly.receipts.steps;

import com.aa.fly.receipts.SpringIntegrationTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FindTicketReceiptDetailOneWayOneConnectionSteps extends SpringIntegrationTest {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find ticket receipt for scenario \"(.+)\"$")
    public void the_client_wants_to_find_ticket_receipt(String scenario) {

    }

    @When("^I search with one-way with one connection ticket number \"(.+)\", last name \"(.+)\", first name \"(.+)\", departure date \"(.+)\"$")
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

    @Then("^I get a successful response with segmentDepartureDate \"([^\"]*)\", firstsegAirportFrom \"([^\"]*)\", firstsegAirportTo \"([^\"]*)\", firstsegTimeFrom \"([^\"]*)\", firstsegTimeTo \"([^\"]*)\", firstflightNum \"([^\"]*)\", secondsegAirportFrom \"([^\"]*)\", secondsegAirportTo \"([^\"]*)\", secondsegTimeFrom \"([^\"]*)\", secondsegTimeTo \"([^\"]*)\", secondflightNum \"([^\"]*)\"$")
    public void i_submit_my_request(String segDepartureDate, String firstSegAirportFrom, String firstSegAirportTo, String firstSegTimeFrom, String firstSegTimeTo, String firstFlightNum, String secondSegAirportFrom, String secondSegAirportTo, String secondSegTimeFrom, String secondSegTimeTo, String secondFlightNum)
            throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        SegmentDetail firstSegmentDetail = ticketReceipt.getSegmentDetails().get(0);
        SegmentDetail secondSegmentDetail = ticketReceipt.getSegmentDetails().get(1);

        Assert.assertEquals(segDepartureDate, dateFormat.format(firstSegmentDetail.getSegmentDepartureDate()));
        Assert.assertEquals(firstSegAirportFrom, firstSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(firstSegAirportTo, firstSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(firstSegTimeFrom, firstSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(firstSegTimeTo, firstSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(firstFlightNum, firstSegmentDetail.getFlightNumber());

        Assert.assertEquals(secondSegAirportFrom, secondSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(secondSegAirportTo, secondSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(secondSegTimeFrom, secondSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(secondSegTimeTo, secondSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(secondFlightNum, secondSegmentDetail.getFlightNumber());
    }
}
