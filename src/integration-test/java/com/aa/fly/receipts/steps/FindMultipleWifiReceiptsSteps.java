package com.aa.fly.receipts.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;

import java.text.ParseException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class FindMultipleWifiReceiptsSteps {

    private static String REQUEST_PAYLOAD;
    private ValidatableResponse validatableResponse;

    @Given("^I want to find multiple wifi receipts$")
    public void i_want_to_find_multiple_wifi_receipts() throws Throwable {

    }

    @When("^I search with last name \"([^\"]*)\", lastFourOfCreditCard \"([^\"]*)\", smaller start date \"([^\"]*)\", end date \"([^\"]*)\"$")
    public void i_search_with_last_name_last_OfCreditCard_smaller_start_date_end_date(String lastName, String last4OfCreditCard, String startDate, String endDate) throws ParseException {

        REQUEST_PAYLOAD = new StringBuffer("{\n")
                .append(" \"lastName\": \"").append(lastName).append("\",\n")
                .append(" \"ccLastFour\": \"").append(last4OfCreditCard).append("\",\n")
                .append(" \"startDate\": \"").append(startDate).append("\",\n")
                .append(" \"endDate\": \"").append(endDate).append("\"\n")
                .append("}").toString();
    }

    @Then("^I get a response$")
    public void i_get_a_response() throws Throwable {

        RestAssured.baseURI = System.getProperty("branch.application.url");

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .body(REQUEST_PAYLOAD)
                .post("/api/wifi-receipt")
                .then();
    }

    @Then("^I get a successful multiple wifi receipts response$")
    public void i_get_a_successful_multiple_wifi_receipts_response() throws Throwable {

        validatableResponse.statusCode(200);
    }

    @Then("^I found two records$")
    public void i_found_two_records() throws Throwable {

        validatableResponse.body("wifiLineItems.orderId", hasItems("0123456789AA", "0123456790AA"));
    }
}
