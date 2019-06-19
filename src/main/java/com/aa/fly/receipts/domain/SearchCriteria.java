/**
 *
 */
package com.aa.fly.receipts.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Shiva.Narendrula
 */
public class SearchCriteria {

    private String ticketNumber;
    private String lastName;
    private String firstName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date departureDate;

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" + "ticketNumber='" + ticketNumber + '\'' + ", lastName='" + lastName + '\''
                + ", firstName='" + firstName + '\'' + ", departureDate=" + departureDate + '}';
    }
}
