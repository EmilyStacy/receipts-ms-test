/**
 *
 */
package com.aa.fly.receipts.domain;

public class PassengerDetail {
    private String ticketNumber;
    private String firstName;
    private String lastName;
    private String advantageNumber;

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
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

    public String getAdvantageNumber() {
        return advantageNumber;
    }

    public void setAdvantageNumber(String advantageNumber) {
        this.advantageNumber = advantageNumber;
    }

    @Override
    public String toString() {
        return "ticketNumber=" + ticketNumber
                + ", firstName=" + firstName + ", lastName=" + lastName + ", advantageNumber=" + advantageNumber;
    }
}
