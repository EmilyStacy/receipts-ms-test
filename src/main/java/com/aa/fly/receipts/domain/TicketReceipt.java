/**
 *
 */
package com.aa.fly.receipts.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Shiva.Narendrula
 */
public class TicketReceipt {
    private String airlineAccountCode;
    private String ticketNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date ticketIssueDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date departureDate;
    private String firstName;
    private String lastName;
    private Airport originAirport;
    private Airport destinationAirport;
    private String pnr;
    private String advantageNumber;

    List<SegmentDetail> segmentDetails = new ArrayList<>();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getAirlineAccountCode() {
        return airlineAccountCode;
    }

    public void setAirlineAccountCode(String airlineAccountCode) {
        this.airlineAccountCode = airlineAccountCode;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Date getTicketIssueDate() {
        return ticketIssueDate;
    }

    public void setTicketIssueDate(Date ticketIssueDate) {
        this.ticketIssueDate = ticketIssueDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Airport getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(Airport originAirport) {
        this.originAirport = originAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getAdvantageNumber() {
        return advantageNumber;
    }

    public void setAdvantageNumber(String advantageNumber) {
        this.advantageNumber = advantageNumber;
    }

    public List<SegmentDetail> getSegmentDetails() {
        return segmentDetails;
    }

    public void setSegmentDetails(List<SegmentDetail> segmentDetails) {
        this.segmentDetails = segmentDetails;
    }

    @Override
    public String toString() {
        return "TicketSummary [airlineAccountCode=" + airlineAccountCode + ", ticketNumber=" + ticketNumber + ", ticketIssueDate=" + dateFormat.format(ticketIssueDate) + ", departureDate="
                + dateFormat.format(departureDate)
                + ", firstName=" + firstName + ", lastName=" + lastName + ", originAirport="
                + originAirport + ", destinationAirport=" + destinationAirport + ", pnr=" + pnr + ", advantageNumber=" + advantageNumber + ", dateFormat=" + dateFormat + ", segmentDetails="
                + segmentDetails + "]";
    }

}
