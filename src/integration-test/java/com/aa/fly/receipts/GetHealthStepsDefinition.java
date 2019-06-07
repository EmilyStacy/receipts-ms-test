package com.aa.fly.receipts;

import org.junit.Assert;
import org.springframework.http.HttpStatus;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GetHealthStepsDefinition extends SpringIntegrationTest {

	@When("^the client calls /health$")
	public void the_client_issues_GET_health() throws Throwable {
		executeGet("https://receipts-ms-dev.apps.depaas.qcorpaa.aa.com/actuator/health");
	}

	@Then("^the client receives response status code of (\\d+)$")
	public void the_client_receives_status_code_of(int statusCode) throws Throwable {
		HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
		Assert.assertEquals(statusCode, currentStatusCode.value());
	}
}
