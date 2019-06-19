package com.aa.fly.receipts;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

//Execute Integration Tests locally as below,
//mvn verify -Pintegration-tests -Dbranch.application.url="https://receipts-ms-dev.apps.depaas.qcorpaa.aa.com"

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/integration-test/resources", format = {"pretty", "html:target/cucumberReport"})
public class IT_CucumberTest {
}