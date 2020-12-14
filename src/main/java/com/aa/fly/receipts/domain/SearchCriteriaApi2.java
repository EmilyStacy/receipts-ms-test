package com.aa.fly.receipts.domain;

public class SearchCriteriaApi2  {
    private String ticketNumber;
    private String lastName;

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

    @Override
    public String toString() {
        return "SearchCriteria{" + "ticketNumber='" + ticketNumber + '\'' + ", lastName='" + lastName
                   + "'}";
}
}
