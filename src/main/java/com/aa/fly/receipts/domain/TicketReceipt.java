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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date ticketIssueDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date departureDate;
    private Airport originAirport;
    private Airport destinationAirport;
    private String pnr;

    List<PassengerDetail> passengerDetails = new ArrayList<>();
    List<SegmentDetail> segmentDetails = new ArrayList<>();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getAirlineAccountCode() {
        return airlineAccountCode;
    }

    public void setAirlineAccountCode(String airlineAccountCode) {
        this.airlineAccountCode = airlineAccountCode;
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

    public List<SegmentDetail> getSegmentDetails() {
        return segmentDetails;
    }

    public void setSegmentDetails(List<SegmentDetail> segmentDetails) {
        this.segmentDetails = segmentDetails;
    }

    public List<PassengerDetail> getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(List<PassengerDetail> passengerDetails) {
        this.passengerDetails = passengerDetails;
    }

    @Override
    public String toString() {
        return "TicketSummary [airlineAccountCode=" + airlineAccountCode + ", ticketIssueDate=" + dateFormat.format(ticketIssueDate) + ", departureDate="
                + dateFormat.format(departureDate) + ", originAirport="
                + originAirport + ", destinationAirport=" + destinationAirport + ", pnr=" + pnr + ", dateFormat=" + dateFormat + ", passengerDetails="
                + passengerDetails + ", segmentDetails=" + segmentDetails + "]";
    }
}
