package com.aa.fly.receipts.steps;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;

public class FindTicketReceiptHeaderSteps extends SpringIntegrationTest {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find my ticket receipt header for scenario \"(.+)\"$")
    public void the_client_want_to_find_ticket_summary(String scenario) {

    }

    @When("^I search with ticket number \"(.+)\", last name \"(.+)\", first name \"(.+)\", departure date \"(.+)\"$")
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

    @Then("^I get a successful response with origin airport \"(.+)\", destinationAirport \"(.+)\" and pnr \"(.+)\"$")
    public void i_submit_my_request(String originAirport, String destinationAirport, String pnr) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        Assert.assertEquals(originAirport, ticketReceipt.getOriginAirportCode());
        Assert.assertEquals(destinationAirport, ticketReceipt.getDestinationAirportCode());
        Assert.assertEquals(pnr, ticketReceipt.getPnr());
    }
}
