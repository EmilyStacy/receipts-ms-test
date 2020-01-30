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
    private String passengerTotalAmount;
    private boolean showPassengerTotal;
    private FareTaxesFees fareTaxesFees;
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

    public String getPassengerTotalAmount() {
        return passengerTotalAmount;
    }

    public void setPassengerTotalAmount(String passengerTotalAmount) {
        this.passengerTotalAmount = passengerTotalAmount;
    }

    public boolean isShowPassengerTotal() {
        return showPassengerTotal;
    }

    public void setShowPassengerTotal(boolean showPassengerTotal) {
        this.showPassengerTotal = showPassengerTotal;
    }

    public List<FormOfPayment> getFormOfPayments() {
        return formOfPayments;
    }

    public void setFormOfPayments(List<FormOfPayment> formOfPayments) {
        this.formOfPayments = formOfPayments;
    }

    public FareTaxesFees getFareTaxesFees() {
        return fareTaxesFees;
    }

    public void setFareTaxesFees(FareTaxesFees fareTaxesFees) {
        this.fareTaxesFees = fareTaxesFees;
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
                + ", passengerTotalAmount=" + passengerTotalAmount + ", showPassangerTotal=" + showPassengerTotal
                + ", fareTaxesFees=" + fareTaxesFees
                + ", loyaltyOwnerCode=" + loyaltyOwnerCode + ", formOfPayments=" + formOfPayments;
    }
}
