package com.aa.fly.receipts.steps;

import com.aa.fly.receipts.SpringIntegrationTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FindTicketReceiptDetailWithMultipleConnectionsSteps extends SpringIntegrationTest {

    private SearchCriteria criteria = new SearchCriteria();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find trip details for scenario \"(.+)\"$")
    public void the_client_wants_to_find_ticket_receipt(String scenario) {

    }

    @When("^I search for my trip with ticket number \"(.+)\", last name \"(.+)\", first name \"(.+)\", departure date \"(.+)\"$")
    public void the_client_enters_ticket_number(String ticketNumber, String lastName, String firstName, String departureDate) {
        String branchApplicationUrl = System.getProperty("branch.application.url");

        criteria.setTicketNumber(ticketNumber);
        criteria.setLastName(lastName);
        criteria.setFirstName(firstName);
        try {
            criteria.setDepartureDate(dateFormat.parse(departureDate));
        } catch (ParseException e) {
        }
        executePost(branchApplicationUrl + "/api/ticket-receipt", criteria);
    }

    @Then("^I get a successful response with pnr as \"([^\"]*)\", departSegmentString as \"([^\"]*)\", returnSegmentString as \"([^\"]*)\"$")
    public void i_submit_my_request(String pnr, String departSegmentString, String returnSegmentString)
            throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponseEntity().getStatusCode();
        Assert.assertEquals(200, currentStatusCode.value());

        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TicketReceipt ticketReceipt = g.fromJson(latestResponse.getBody(), TicketReceipt.class);

        Assert.assertEquals(pnr, ticketReceipt.getPnr());
        Assert.assertEquals(returnSegmentString.trim(), getSliceString(ticketReceipt.getSegmentDetails(), true));
        Assert.assertEquals(departSegmentString.trim(), getSliceString(ticketReceipt.getSegmentDetails(), false));
    }

    private String getSliceString(List<SegmentDetail> segmentDetails, boolean isReturn) {
        String onwardSliceString = "";
        String returnSliceString = "";
        boolean newSlice = false;
        int segmentCount = 0;
        for (SegmentDetail segment : segmentDetails) {
            if ("true".equals(segment.getReturnTrip()) && segmentCount > 0) {
                newSlice = true;
            }
            if (newSlice) {
                returnSliceString = returnSliceString + buildSegmentString(segment) + ". ";
            } else {
                onwardSliceString = onwardSliceString + buildSegmentString(segment) + ". ";
            }
            segmentCount++;
        }
        return isReturn ? returnSliceString.trim() : onwardSliceString.trim();
    }

    private String buildSegmentString(SegmentDetail segment) {
        //AA4063 leaving TYR to DFW on 2019-10-26 at 06:45:00 and arriving on 2019-10-26 at 07:48:00 in class OWBVZNB5
        StringBuilder sb =  new StringBuilder(segment.getCarrierCode());
        sb.append(segment.getFlightNumber());
        sb.append(" leaving ");
        sb.append(segment.getDepartureAirport().getCode());
        sb.append("(");
        sb.append(segment.getDepartureAirport().getCity());
        sb.append(", ");
        if ("US".equals(segment.getDepartureAirport().getCountryCode())) {
            sb.append(segment.getDepartureAirport().getStateCode());
        } else {
            sb.append(segment.getDepartureAirport().getCountryName());
        }
        sb.append(") to ");
        sb.append(segment.getArrivalAirport().getCode());
        sb.append("(");
        sb.append(segment.getArrivalAirport().getCity());
        sb.append(", ");
        if ("US".equals(segment.getArrivalAirport().getCountryCode())) {
            sb.append(segment.getArrivalAirport().getStateCode());
        } else {
            sb.append(segment.getArrivalAirport().getCountryName());
        }
        sb.append(") on ");
        sb.append(dateFormat.format(segment.getSegmentDepartureDate()));
        sb.append(" at ");
        sb.append(segment.getSegmentDepartureTime());
        sb.append(" and arriving on ");
        sb.append(dateFormat.format(segment.getSegmentArrivalDate()));
        sb.append(" at ");
        sb.append(segment.getSegmentArrivalTime());
        sb.append(" in class ");
        sb.append(segment.getFareBasis());
        return sb.toString();
    }
}
