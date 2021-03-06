package com.aa.fly.receipts.steps;

import java.text.SimpleDateFormat;

import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.TicketReceipt;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;

public class FindTicketReceiptHeaderSteps extends SpringIntegrationSetup {

    private SearchCriteriaApi2 criteriaApi2 = new SearchCriteriaApi2();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find my ticket receipt header for scenario \"(.+)\"$")
    public void the_client_wants_to_find_ticket_receipt(String scenario) {

    }

    @When("^I search with ticket number \"([^\"]*)\", last name \"([^\"]*)\"$")
    public void the_client_enters_ticket_number(String ticketNumber, String lastName) {
        String branchApplicationUrl = System.getProperty("branch.application.url");

        criteriaApi2.setTicketNumber(ticketNumber);
        criteriaApi2.setLastName(lastName);

        executePost(branchApplicationUrl + "/api2/ticket-receipt", criteriaApi2);
    }

    @Then("^I get a successful response with origin airport \"([^\"]*)\", destinationAirport \"([^\"]*)\" and pnr \"([^\"]*)\" and advantageNumber \"([^\"]*)\" and loyaltyOwnerCode \"([^\"]*)\"$")
    public void i_submit_my_request(String originAirport, String destinationAirport, String pnr, String advantageNumber, String loyaltyOwnerCode) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        Assert.assertEquals(originAirport, ticketReceipt.getOriginAirport().getCode());
        Assert.assertEquals(destinationAirport, ticketReceipt.getDestinationAirport().getCode());
        Assert.assertEquals(pnr, ticketReceipt.getPnr());
        Assert.assertEquals(advantageNumber, ticketReceipt.getPassengerDetails().get(0).getAdvantageNumber()==null?"":ticketReceipt.getPassengerDetails().get(0).getAdvantageNumber());
        Assert.assertEquals(loyaltyOwnerCode, ticketReceipt.getPassengerDetails().get(0).getLoyaltyOwnerCode()==null?"":ticketReceipt.getPassengerDetails().get(0).getLoyaltyOwnerCode());
    }
}
