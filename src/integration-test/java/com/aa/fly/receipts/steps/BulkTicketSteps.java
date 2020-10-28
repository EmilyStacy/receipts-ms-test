package com.aa.fly.receipts.steps;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.StatusMessage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;

public class BulkTicketSteps extends SpringIntegrationSetup {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to retrieve a bulk ticket receipt for scenario \"([^\"]*)\"$")
    public void i_want_to_retrieve_a_bulk_ticket_receipt_for_scenario(String arg1) throws Throwable {

    }

    @Then("^I get a response with bulk ticket found message pnr \"([^\"]*)\", statusMessage \"([^\"]*)\"$")
    public void i_get_a_response_with_bulk_ticket_found_message_pnr_statusMessage(String pnr, String statusMessage) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        Assert.assertEquals(pnr, ticketReceipt.getPnr());
        Assert.assertEquals(statusMessage, StatusMessage.BULK_TICKET.getStatusMessage());
    }

}
