package com.aa.fly.receipts.steps;

import com.aa.fly.receipts.SpringIntegrationSetup;
import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.List;

public class FindTicketReceiptDetailWithMultipleConnectionsSteps extends SpringIntegrationSetup {

    private SearchCriteriaApi2 criteriaApi2 = new SearchCriteriaApi2();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Given("^I want to find trip details for scenario \"(.+)\"$")
    public void the_client_wants_to_find_ticket_receipt(String scenario) {

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
        StringBuilder onwardSliceString = new StringBuilder();
        StringBuilder returnSliceString = new StringBuilder();
        boolean newSlice = false;
        int segmentCount = 0;
        for (SegmentDetail segment : segmentDetails) {
            if ("true".equals(segment.getReturnTrip()) && segmentCount > 0) {
                newSlice = true;
            }
            if (newSlice) {
                returnSliceString.append(buildSegmentString(segment)).append(". ");
            } else {
                onwardSliceString.append(buildSegmentString(segment)).append(". ");
            }
            segmentCount++;
        }
        return isReturn ? returnSliceString.toString().trim() : onwardSliceString.toString().trim();
    }

    private String buildSegmentString(SegmentDetail segment) {
        // AA4063 leaving TYR to DFW on 2019-10-26 at 06:45:00 and arriving on 2019-10-26 at 07:48:00 in class OWBVZNB5
        StringBuilder sb = new StringBuilder(segment.getCarrierCode());
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
        sb.append(" and the flight status is ");
        sb.append(segment.getSegmentStatus());
        return sb.toString();
    }
}
