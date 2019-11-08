package com.aa.fly.receipts.steps;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;

public class FindWifiReceiptsSteps extends SpringIntegrationSetup {

    private WifiSearchCriteria criteria = new WifiSearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find my wifi subscription$")
    public void the_client_want_to_find_receipts() {

    }

    @When("^I search with CC last four digits \"(.+)\", last name \"(.+)\", start date \"(.+)\", end date \"(.+)\"$")
    public void the_client_enters_ticket_number(String ccLastFour, String lastName, String startDate, String endDate) {
        String branchApplicationUrl = System.getProperty("branch.application.url");

        criteria.setCcLastFour(ccLastFour);
        criteria.setLastName(lastName);
        try {
            criteria.setStartDate(dateFormat.parse(startDate));
            criteria.setEndDate(dateFormat.parse(endDate));
        } catch (ParseException e) {
        }
        executePost(branchApplicationUrl + "/api/wifi-receipt", criteria);
    }

    @Then("^I get a successful response$")
    public void i_get_a_successful_response() throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());
    }

    @Then("I find number of subscription records \"(.+)\"$")
    public void the_client_receives_status_code_of(String noOfRecords) throws Throwable {
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        WifiReceipt response = g.fromJson(latestResponse.getBody(), WifiReceipt.class);
        Assert.assertTrue(response.getWifiLineItems() != null);
        Assert.assertEquals(noOfRecords, response.getWifiLineItems().size() + "");
    }
}
