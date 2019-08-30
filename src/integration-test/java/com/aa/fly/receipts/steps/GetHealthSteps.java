package com.aa.fly.receipts.steps;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import com.aa.fly.receipts.SpringIntegrationTest;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GetHealthSteps extends SpringIntegrationTest {

    @When("^the client calls /actuator/health$")
    public void the_client_issues_GET_health() throws Throwable {
        String branchApplicationUrl = System.getProperty("branch.application.url");
        System.out.println("branch.application.url used to run IT: " + branchApplicationUrl);

        executeGet(branchApplicationUrl + "/actuator/health");
    }

    @Then("^the client receives response status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        Assert.assertEquals(statusCode, currentStatusCode.value());
    }
}
