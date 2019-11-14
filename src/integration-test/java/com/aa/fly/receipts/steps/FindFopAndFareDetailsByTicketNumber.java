package com.aa.fly.receipts.steps;

import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;

public class FindFopAndFareDetailsByTicketNumber extends SpringIntegrationSetup {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to retrieve payment details for scenario \"([^\"]*)\"$")
    public void i_want_to_retrieve_payment_details_for_scenario(String arg1) throws Throwable {

    }

    @Then("^I get a successful response with fopIssueDate \"([^\"]*)\", fopTypeCode \"([^\"]*)\",  fopTypeDescription \"([^\"]*)\",  fopAccountNumberLastFour \"([^\"]*)\", fopAmount \"([^\"]*)\", and fopCurrencyCode \"([^\"]*)\"$")
    public void i_get_a_successful_response_with_fopIssueDate_fopTypeCode_fopTypeDescription_fopAccountNumberLast_fopAmount_and_fopCurrencyCode(String fopIssueDate, String fopTypeCode,
            String fopTypeDescription, String fopAccountNumberLastFour, String fopAmount, String fopCurrencyCode) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        FormOfPayment fop = ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0);
        Assert.assertEquals(fopIssueDate, dateFormat.format(fop.getFopIssueDate()));
        Assert.assertEquals(fopTypeCode, fop.getFopTypeCode());
        Assert.assertEquals(fopTypeDescription, fop.getFopTypeDescription());
        Assert.assertEquals(fopAccountNumberLastFour, fop.getFopAccountNumberLast4());
        Assert.assertEquals(fopAmount, fop.getFopAmount());
        Assert.assertEquals(fopCurrencyCode, fop.getFopCurrencyCode());
    }

    @Then("^I get a successful response with baseFareAmount \"([^\"]*)\", baseFareCurrencyCode \"([^\"]*)\", and totalFareAmount \"([^\"]*)\"$")
    public void i_get_a_successful_response_with_baseFareAmount_baseFareCurrencyCode_and_totalFareAmount(String baseFareAmount, String baseFareCurrencyCode, String totalFareAmount) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);

        FareTaxesFees fareTaxesFees = ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees();
        Assert.assertEquals(baseFareAmount, fareTaxesFees.getBaseFareAmount());
        Assert.assertEquals(baseFareCurrencyCode, fareTaxesFees.getBaseFareCurrencyCode());
        Assert.assertEquals(totalFareAmount, fareTaxesFees.getTotalFareAmount());
    }

}
