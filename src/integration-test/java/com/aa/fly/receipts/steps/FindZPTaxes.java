package com.aa.fly.receipts.steps;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.TicketReceipt;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class FindZPTaxes extends SpringIntegrationSetup {
    private SearchCriteriaApi2 criteriaApi2 = new SearchCriteriaApi2();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to retrieve ZP taxes details for scenario \"(.+)\"$")
    public void I_want_to_find_ticket_receipt_with_zp(String scenario) {

    }

    @Then("^I get a successful response with the correct taxamount \"([^\"]*)\" and zpamount \"([^\"]*)\"$")
    public void iGetASuccessfulResponseWithTheCorrectTaxamountAndZpamount(String taxamount, String zpamount) throws Throwable {
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);

        FareTaxesFees fareTaxesFees = ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees();

        String zpTax = fareTaxesFees.getTaxes().stream().filter(tax -> "ZP".equals(tax.getTaxCode())).collect(Collectors.toSet()).toString();
        if (zpTax == null) {
            zpTax = "";
        }

        Assert.assertEquals(taxamount, fareTaxesFees.getTaxFareAmount());
        Assert.assertEquals(zpamount, zpTax);

    }
}
