package com.aa.fly.receipts.steps;

import java.util.Map;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.SearchCriteria;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConfirmOlderThan18Month204Steps extends SpringIntegrationSetup {

    private SearchCriteria criteria = new SearchCriteria();

    @Given("^I want to confirm Mosaic only supports tickets with issued date not older than (\\d+) months from today$")
    public void i_want_to_confirm_Mosaic_only_supports_tickets_with_issued_date_not_older_than_months_from_today(int arg1) throws Throwable {
    }

    @When("^I search ticket number with below criteria$")
    public void i_search_ticket_number_with_below_criteria(Map< String, String > testDataMap) throws Throwable {
        String branchApplicationUrl = System.getProperty("branch.application.url");

        criteria.setTicketNumber(testDataMap.get("ticketNumber"));
        criteria.setLastName(testDataMap.get("lastName"));
        criteria.setFirstName(testDataMap.get("firstName"));
        criteria.setDepartureDate(testDataMap.get("departureDate"));

        executePost(branchApplicationUrl + "/api/ticket-receipt", criteria);
    }

    @Then("^I get a (\\d+) http status code response indicating no content$")
    public void i_get_a_http_status_code_response_indicating_no_content(int httpStatusCode) throws Throwable {
        
    	HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(httpStatusCode, currentStatusCode.value());
    }
}
