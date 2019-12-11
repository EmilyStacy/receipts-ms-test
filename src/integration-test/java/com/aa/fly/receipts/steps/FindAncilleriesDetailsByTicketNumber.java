package com.aa.fly.receipts.steps;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.TicketReceipt;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;

public class FindAncilleriesDetailsByTicketNumber extends SpringIntegrationSetup {

    @Given("^I want to retrieve payment details - ancillaries for scenario \"([^\"]*)\"$")
    public void i_want_to_retrieve_payment_details_ancillaries_for_scenario(String arg1) throws Throwable {

    }

    @Then("^I get a successful response with ancillaries rowCount \"([^\"]*)\", fopAmt \"([^\"]*)\", totalFareAmount \"([^\"]*)\", passengerTotalAmount \"([^\"]*)\"$")
    public void i_get_a_successful_response_with_ancillaries_rowCount(String rowCount, String fopAmt, String totalFareAmount, String passengerTotalAmount) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);

        Assert.assertTrue(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getAncillaries().isEmpty());
        Assert.assertEquals(fopAmt, ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
        Assert.assertEquals(totalFareAmount, ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
        Assert.assertEquals(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount(), ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());

        Assert.assertEquals(passengerTotalAmount, ticketReceipt.getPassengerDetails().get(0).getPassengerTotalAmount());
        Assert.assertEquals(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount(), ticketReceipt.getPassengerDetails().get(0).getPassengerTotalAmount());
    }
}
