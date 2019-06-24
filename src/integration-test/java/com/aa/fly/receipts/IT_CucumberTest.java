package com.aa.fly.receipts;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

// New Integration Tests execution format,
// mvn verify -Pintegration-tests -Dbranch.application.url="https://receipts-ms-dev.apps.depaas.qcorpaa.aa.com"
// to test any new operations that are in local, but not deployed to dev, use the following command
// mvn verify -Pintegration-tests -Dcucumber.options='--tags ~@TicketAndFees' -Dbranch.application.url="http://localhost:8094"

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/integration-test/resources", format = {"pretty", "html:target/cucumberReport"})
public class IT_CucumberTest {
}