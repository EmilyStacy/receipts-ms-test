/**
 *
 */
package com.aa.fly.receipts.domain;

/**
 * @author Shiva.Narendrula
 */
public class SearchCriteria {

    private String ticketNumber;
    private String pnr;
    private String lastName;
    private String firstName;
    private String departureDate;

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

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" + "ticketNumber='" + ticketNumber + '\'' + ", lastName='" + lastName + '\''
                + ", firstName='" + firstName + '\'' + ", departureDate=" + departureDate + '\'' + ", pnr=" + pnr + '}';
    }
}
