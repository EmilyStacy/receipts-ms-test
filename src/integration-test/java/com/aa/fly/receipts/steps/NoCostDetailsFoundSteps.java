package com.aa.fly.receipts.steps;

import java.text.SimpleDateFormat;

import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.TicketReceipt;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;

public class NoCostDetailsFoundSteps extends SpringIntegrationSetup {

    private SearchCriteriaApi2 criteriaApi2 = new SearchCriteriaApi2();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to retrieve ticket receipt for scenario \"([^\"]*)\"$")
    public void i_want_to_retrieve_ticket_receipt_for_scenario(String arg1) throws Throwable {

    }

    @Then("^I get a response with no cost details found message pnr \"([^\"]*)\", statusMessage \"([^\"]*)\"$")
    public void i_get_a_response_with_no_cost_details_found_message_pnr_statusMessage(String pnr, String statusMessage) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        Assert.assertEquals(pnr, ticketReceipt.getPnr());
        Assert.assertEquals(statusMessage, ticketReceipt.getStatusMessage());
    }

}
