package com.aa.fly.receipts.steps;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.StatusMessage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.springframework.http.HttpStatus;


public class AgencyAndBulkTicketSteps extends SpringIntegrationSetup {

    @Given("^I want to retrieve a bulk or agency ticket receipt for scenario \"([^\"]*)\"$")
    public void i_want_to_retrieve_a_bulk_ticket_receipt_for_scenario(String arg1) throws Throwable {

    }

    @Then("^I get a response with a status message \"([^\"]*)\"$")
    public void i_get_a_response_with_bulk_ticket_found_message_pnr_statusMessage(String statusMessage) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        Assert.assertEquals(statusMessage, statusMessage.contains("Bulk")?StatusMessage.BULK_TICKET.getStatusMessage(): StatusMessage.AGENCY_TICKET.getStatusMessage());
        Assert.assertEquals(statusMessage, ticketReceipt.getStatusMessage());
    }

}
