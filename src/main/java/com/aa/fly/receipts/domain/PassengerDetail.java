/**
 *
 */
package com.aa.fly.receipts.domain;

import java.util.List;

public class PassengerDetail {
    private String ticketNumber;
    private String firstName;
    private String lastName;
    private String loyaltyOwnerCode;
    private String advantageNumber;
    private List<FormOfPayment> formOfPayments;

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

    public List<FormOfPayment> getFormOfPayments() {
        return formOfPayments;
    }

    public void setFormOfPayments(List<FormOfPayment> formOfPayments) {
        this.formOfPayments = formOfPayments;
    }

    public String getLoyaltyOwnerCode() {
        return loyaltyOwnerCode;
    }

    public void setLoyaltyOwnerCode(String loyaltyOwnerCode) {
        this.loyaltyOwnerCode = loyaltyOwnerCode;
    }

    @Override
    public String toString() {
        return "ticketNumber=" + ticketNumber
                + ", firstName=" + firstName + ", lastName=" + lastName + ", advantageNumber=" + advantageNumber
                + ", loyaltyOwnerCode=" + loyaltyOwnerCode + ", formOfPayments=" + formOfPayments ;
    }

}
