package com.aa.fly.receipts.steps;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindAncillariesDetailsByTicketNumber extends SpringIntegrationSetup {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Then("^I get a successful response with the following ancillaries$")
    public void iGetASuccessfulResponseWithTheFollowingAncillaries(Map< String, String > testDataMap) throws Throwable {
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        BigDecimal fopSummary = BigDecimal.valueOf(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().stream().map(i -> Double.parseDouble(i.getFopAmount())).reduce(0.00, Double::sum))
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        Assert.assertEquals(new BigDecimal(testDataMap.get("passengerTotalAmount")), fopSummary);
        i_get_a_successful_response_with_ancillaries_and_fop_details_fopIssueDate_fopTypeCode_fopTypeDescription_fopAccountNumberLast_fopAmount_and_fopCurrencyCode(testDataMap.get("anclryFOP1IssueDate"), testDataMap.get("anclryFOP1TypeCode"),
                testDataMap.get("anclryFOP1AccountDescription"), testDataMap.get("anclryFOP1AccountNumLastFour"),testDataMap.get("anclryFOPAmt1"), testDataMap.get("anclryFOPAmt1CurrencyCode"), 1);
        i_get_a_successful_response_with_ancillaries_and_fop_details_fopIssueDate_fopTypeCode_fopTypeDescription_fopAccountNumberLast_fopAmount_and_fopCurrencyCode(testDataMap.get("anclryFOP2IssueDate"), testDataMap.get("anclryFOP2TypeCode"),
                testDataMap.get("anclryFOP2AccountDescription"), testDataMap.get("anclryFOP2AccountNumLastFour"),testDataMap.get("anclryFOPAmt2"), testDataMap.get("anclryFOPAmt2CurrencyCode"), 2);
        i_get_a_successful_response_with_ancillaries_and_fop_details_fopIssueDate_fopTypeCode_fopTypeDescription_fopAccountNumberLast_fopAmount_and_fopCurrencyCode(testDataMap.get("anclryFOP3IssueDate"), testDataMap.get("anclryFOP3TypeCode"),
                testDataMap.get("anclryFOP3AccountDescription"), testDataMap.get("anclryFOP3AccountNumLastFour"),testDataMap.get("anclryFOPAmt3"), testDataMap.get("anclryFOPAmt3CurrencyCode"), 5);
        Assert.assertEquals(testDataMap.get("anclryFOPName1"),getAncillaryItem(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments(),1,0).getAnclryProdName());
        Assert.assertEquals(testDataMap.get("anclryFOPName2"),getAncillaryItem(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments(),2,0).getAnclryProdName());
        Assert.assertEquals(testDataMap.get("anclryFOPName3"),getAncillaryItem(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments(),5,0).getAnclryProdName());
    }

    @Given("^I want to retrieve payment details - ancillaries for scenario \"([^\"]*)\"$")
    public void i_want_to_retrieve_payment_details_ancillaries_for_scenario(String arg1) throws Throwable {

    }

    @Then("^I get a successful response without ancillaries rowCount \"([^\"]*)\", fopAmt \"([^\"]*)\", totalFareAmount \"([^\"]*)\", passengerTotalAmount \"([^\"]*)\"$")
    public void i_get_a_successful_response_without_ancillaries_rowCount(String rowCount, String fopAmt, String totalFareAmount, String passengerTotalAmount) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);

        Assert.assertTrue(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getAncillaries().isEmpty());
        Assert.assertEquals(fopAmt, ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
        Assert.assertEquals(totalFareAmount, ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
        Assert.assertEquals(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount(), ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());

        Assert.assertEquals(passengerTotalAmount, ticketReceipt.getPassengerDetails().get(0).getPassengerTotalAmount());
        Assert.assertEquals(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount(), ticketReceipt.getPassengerDetails().get(0).getPassengerTotalAmount());

    }

    @Then("^I get a successful response with one ancillary rowCount \"([^\"]*)\", fopAmt \"([^\"]*)\", passengerTotalAmount \"([^\"]*)\", anclryFOPAmt1 \"([^\"]*)\", anclryFOPAmt2 \"([^\"]*)\", anclryFOPIssueDate \"([^\"]*)\", anclryFOPTypeCode \"([^\"]*)\", anclryFOPAccountNumberLast4 \"([^\"]*)\", anclryDocNbr \"([^\"]*)\", anclryIssueDate \"([^\"]*)\", anclryPriceCurrencyAmount \"([^\"]*)\", anclrySalesCurrencyAmount \"([^\"]*)\", anclryTaxCurrencyAmount \"([^\"]*)\"$")
    public void i_get_a_successful_response_with__one_ancillary_rowCount_fopAmt_totalFareAmount_passengerTotalAmount_anclryDocNbr_anclryIssueDate_anclryPriceCurrencyAmount_anclrySalesCurrencyAmount_anclryTaxCurrencyAmount(
            String rowCount, String fopAmt, String passengerTotalAmount, String anclryFOPAmt1, String anclryFOPAmt2, String anclryFOPIssueDate, String anclryFOPTypeCode,
            String anclryFOPAccountNumberLast4, String anclryDocNbr, String anclryIssueDate, String anclryPriceCurrencyAmount,
            String anclrySalesCurrencyAmount, String anclryTaxCurrencyAmount) throws Throwable {

        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);

        Assert.assertEquals(Integer.parseInt(rowCount), ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().size());
        Assert.assertEquals(new BigDecimal(passengerTotalAmount), new BigDecimal(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount())
                .add(new BigDecimal(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(1).getFopAmount())));
        Ancillary ancillary = new Ancillary(anclryDocNbr, "", null, "", "", "", "", "", "");
        Assert.assertTrue(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(1).getAncillaries().contains(ancillary));

        Ancillary[] ancillaries = ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(1).getAncillaries().toArray(new Ancillary[0]);
        Assert.assertEquals(anclryIssueDate, ancillaries[0].getAnclryIssueDate());
        Assert.assertEquals(anclryPriceCurrencyAmount, ancillaries[0].getAnclryPriceCurrencyAmount());
        Assert.assertEquals(anclrySalesCurrencyAmount, ancillaries[0].getAnclrySalesCurrencyAmount());
        Assert.assertEquals(anclryTaxCurrencyAmount, ancillaries[0].getAnclryTaxCurrencyAmount());
    }

    @Then("^I get a successful response with two ancillaries rowCount \"([^\"]*)\",passengerTotalAmount \"([^\"]*)\",anclryDocNbrOne \"([^\"]*)\",anclryIssueDateone \"([^\"]*)\",anclryProdCodeOne \"([^\"]*)\",anclryProdNameOne \"([^\"]*)\",anclryPriceCurrencyAmountOne \"([^\"]*)\",anclryPriceCurrencyCodeOne \"([^\"]*)\",anclrySalesCurrencyAmountOne \"([^\"]*)\",anclrySalesCurrencyCodeOne \"([^\"]*)\",anclryTaxCurrencyAmountOne \"([^\"]*)\",anclryDocNbrTwo \"([^\"]*)\",anclryIssueDateTwo \"([^\"]*)\",anclryProdCodeTwo \"([^\"]*)\",anclryProdNameTwo \"([^\"]*)\",anclryPriceCurrencyAmountTwo \"([^\"]*)\",anclryPriceCurrencyCodeTwo \"([^\"]*)\",anclrySalesCurrencyAmountTwo \"([^\"]*)\",anclrySalesCurrencyCodeTwo \"([^\"]*)\",anclryTaxCurrencyAmountTwo \"([^\"]*)\"$")
    public void response_with_two_ancillaries_bought_ticket_with_3fops(String rowCount, String passengerTotalAmount,
                                                                       String anclryDocNbrOne, String anclryIssueDateone, String anclryProdCodeOne, String anclryProdNameOne, String anclryPriceCurrencyAmountOne, String anclryPriceCurrencyCodeOne, String anclrySalesCurrencyAmountOne, String anclrySalesCurrencyCodeOne, String anclryTaxCurrencyAmountOne,
                                                                       String anclryDocNbrTwo, String anclryIssueDateTwo, String anclryProdCodeTwo, String anclryProdNameTwo, String anclryPriceCurrencyAmountTwo, String anclryPriceCurrencyCodeTwo, String anclrySalesCurrencyAmountTwo, String anclrySalesCurrencyCodeTwo, String anclryTaxCurrencyAmountTwo) throws Throwable {

        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);

        Assert.assertEquals(Integer.parseInt(rowCount), ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().size());
        Assert.assertEquals(new BigDecimal(passengerTotalAmount), new BigDecimal(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount())
                .add(new BigDecimal(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(1).getFopAmount()))
                .add(new BigDecimal(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(2).getFopAmount())));
        String PassengerTotalAmount = ticketReceipt.getPassengerDetails().get(0).getPassengerTotalAmount();
        Assert.assertEquals(passengerTotalAmount, PassengerTotalAmount);

        Ancillary[] ancillaries1 = ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(1).getAncillaries().toArray(new Ancillary[0]);
        Assert.assertEquals(anclryDocNbrOne, ancillaries1[0].getAnclryDocNbr());
        Assert.assertEquals(anclryIssueDateone, ancillaries1[0].getAnclryIssueDate());
        Assert.assertEquals(anclryProdCodeOne, ancillaries1[0].getAnclryProdCode());
        Assert.assertEquals(anclryProdNameOne, ancillaries1[0].getAnclryProdName());
        Assert.assertEquals(anclryPriceCurrencyAmountOne, ancillaries1[0].getAnclryPriceCurrencyAmount());
        Assert.assertEquals(anclryPriceCurrencyCodeOne, ancillaries1[0].getAnclryPriceCurrencyCode());
        Assert.assertEquals(anclrySalesCurrencyAmountOne, ancillaries1[0].getAnclrySalesCurrencyAmount());
        Assert.assertEquals(anclrySalesCurrencyCodeOne, ancillaries1[0].getAnclrySalesCurrencyCode());
        Assert.assertEquals(anclryTaxCurrencyAmountOne, ancillaries1[0].getAnclryTaxCurrencyAmount());

        Ancillary[] ancillaries2 = ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(2).getAncillaries().toArray(new Ancillary[0]);
        Assert.assertEquals(anclryDocNbrTwo, ancillaries2[0].getAnclryDocNbr());
        Assert.assertEquals(anclryIssueDateTwo, ancillaries2[0].getAnclryIssueDate());
        Assert.assertEquals(anclryProdCodeTwo, ancillaries2[0].getAnclryProdCode());
        Assert.assertEquals(anclryProdNameTwo, ancillaries2[0].getAnclryProdName());
        Assert.assertEquals(anclryPriceCurrencyAmountTwo, ancillaries2[0].getAnclryPriceCurrencyAmount());
        Assert.assertEquals(anclryPriceCurrencyCodeTwo, ancillaries2[0].getAnclryPriceCurrencyCode());
        Assert.assertEquals(anclrySalesCurrencyAmountTwo, ancillaries2[0].getAnclrySalesCurrencyAmount());
        Assert.assertEquals(anclrySalesCurrencyCodeTwo, ancillaries2[0].getAnclrySalesCurrencyCode());
        Assert.assertEquals(anclryTaxCurrencyAmountTwo, ancillaries2[0].getAnclryTaxCurrencyAmount());

    }


    @Then("^I get a successful response with ancillaries and fop details fopIssueDate \"([^\"]*)\", fopTypeCode \"([^\"]*)\", fopTypeDescription \"([^\"]*)\", fopAccountNumberLastFour \"([^\"]*)\", fopAmount \"([^\"]*)\", fopCurrencyCode \"([^\"]*)\" and fopIndex \"([^\"]*)\"$")
    public void i_get_a_successful_response_with_ancillaries_and_fop_details_fopIssueDate_fopTypeCode_fopTypeDescription_fopAccountNumberLast_fopAmount_and_fopCurrencyCode(String fopIssueDate, String fopTypeCode,
                                                                                                                                                                            String fopTypeDescription, String fopAccountNumberLastFour, String fopAmount, String fopCurrencyCode, int fopIndex) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        FormOfPayment fop = ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(fopIndex);
        Assert.assertEquals(fopIssueDate, dateFormat.format(fop.getFopIssueDate()));
        Assert.assertEquals(fopTypeCode, fop.getFopTypeCode());
        Assert.assertEquals(fopTypeDescription, fop.getFopTypeDescription());
        Assert.assertEquals(fopAccountNumberLastFour, fop.getFopAccountNumberLast4());
        Assert.assertEquals(fopAmount, fop.getFopAmount());
        Assert.assertEquals(fopCurrencyCode, fop.getFopCurrencyCode());
    }

    @Then("^I get a successful response with invalid airline code rowCount \"([^\"]*)\"")
    public void i_get_a_successful_response_with_invalid_airline_code(String rowCount) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());
    }

    private Ancillary getAncillaryItem(List<FormOfPayment> formOfPayments, int fopIndex, int ancillaryIndex)
    {
        Set<Ancillary> ancillaries = formOfPayments.get(fopIndex).getAncillaries();
        return new ArrayList<>(ancillaries).get(ancillaryIndex);
    }

    @Then("^I get a successful response with the following ancillaries and ticket information$")
    public void iGetASuccessfulResponseWithTheFollowingAncillariesAndTicketInformation(Map< String, String > testDataMap) throws Throwable {
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);
        Assert.assertEquals(testDataMap.get("ticketIssueDate"), dateFormat.format(ticketReceipt.getTicketIssueDate()));

        i_get_a_successful_response_with_ancillaries_and_fop_details_fopIssueDate_fopTypeCode_fopTypeDescription_fopAccountNumberLast_fopAmount_and_fopCurrencyCode(testDataMap.get("ticketIssueDate"), testDataMap.get("ticketFOPTypeCode"),
                testDataMap.get("ticketFOPAccountDescription"), testDataMap.get("ticketFOPAccountNumLastFour"),testDataMap.get("ticketFOPAmt"), testDataMap.get("ticketFOPAmtCurrencyCode"), 0);

        i_get_a_successful_response_with_ancillaries_and_fop_details_fopIssueDate_fopTypeCode_fopTypeDescription_fopAccountNumberLast_fopAmount_and_fopCurrencyCode(testDataMap.get("anclryFOP1IssueDate"), testDataMap.get("anclryFOP1TypeCode"),
                testDataMap.get("anclryFOP1AccountDescription"), testDataMap.get("anclryFOP1AccountNumLastFour"),testDataMap.get("anclryFOPAmt1"), testDataMap.get("anclryFOPAmt1CurrencyCode"), 2);
        Assert.assertEquals(testDataMap.get("anclryFOPName1"),getAncillaryItem(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments(),2,0).getAnclryProdName());
    }
}

