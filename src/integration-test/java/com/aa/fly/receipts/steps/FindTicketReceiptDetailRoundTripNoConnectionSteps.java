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

public class FindTicketReceiptDetailRoundTripNoConnectionSteps extends SpringIntegrationTest {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find round trip ticket receipt for scenario \"(.+)\"$")
    public void the_client_wants_to_find_ticket_receipt(String scenario) {

    }

    @When("^I search for round trip with no connection ticket number \"(.+)\", last name \"(.+)\", first name \"(.+)\", departure date \"(.+)\"$")
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

    @Then("^I get a successful response for round trip ticket receipt with firstSegDepartureDate \"([^\"]*)\", firstSegAirportFrom \"([^\"]*)\", firstSegAirportTo \"([^\"]*)\", firstSegTimeFrom \"([^\"]*)\", firstSegTimeTo \"([^\"]*)\", firstSegFlightNum \\\"([^\\\"]*)\\\", firstSegCouponSeqNumber \\\"([^\\\"]*)\\\", secondSegDepartureDate \\\"([^\\\"]*)\\\", secondSegAirportFrom \\\"([^\\\"]*)\\\", secondSegAirportTo \"([^\"]*)\", secondSegTimeFrom \"([^\"]*)\", secondSegTimeTo \"([^\"]*)\", secondSegFlightNum \"([^\"]*)\", secondSegCouponSeqNumber \"([^\"]*)\"$")
    public void i_submit_my_request(String firstSegDepartureDate, String firstSegAirportFrom, String firstSegAirportTo, String firstSegTimeFrom, String firstSegTimeTo, String firstSegFlightNum,
            String firstSegCouponSeqNumber, String secondSegDepartureDate,
            String secondSegAirportFrom, String secondSegAirportTo, String secondSegTimeFrom, String secondSegTimeTo, String secondSegFlightNum, String secondSegCouponSeqNumber)
            throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        SegmentDetail firstSegmentDetail = ticketReceipt.getSegmentDetails().get(0);
        SegmentDetail secondSegmentDetail = ticketReceipt.getSegmentDetails().get(1);

        Assert.assertEquals(firstSegDepartureDate, dateFormat.format(firstSegmentDetail.getSegmentDepartureDate()));
        Assert.assertEquals(firstSegAirportFrom, firstSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(firstSegAirportTo, firstSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(firstSegTimeFrom, firstSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(firstSegTimeTo, firstSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(firstSegFlightNum, firstSegmentDetail.getFlightNumber());
        Assert.assertEquals(firstSegCouponSeqNumber, firstSegmentDetail.getCouponSeqNumber());

        Assert.assertEquals(secondSegDepartureDate, dateFormat.format(secondSegmentDetail.getSegmentDepartureDate()));
        Assert.assertEquals(secondSegAirportFrom, secondSegmentDetail.getSegmentDepartureAirportCode());
        Assert.assertEquals(secondSegAirportTo, secondSegmentDetail.getSegmentArrivalAirportCode());
        Assert.assertEquals(secondSegTimeFrom, secondSegmentDetail.getSegmentDepartureTime());
        Assert.assertEquals(secondSegTimeTo, secondSegmentDetail.getSegmentArrivalTime());
        Assert.assertEquals(secondSegFlightNum, secondSegmentDetail.getFlightNumber());
        Assert.assertEquals(secondSegCouponSeqNumber, secondSegmentDetail.getCouponSeqNumber());
    }
}
