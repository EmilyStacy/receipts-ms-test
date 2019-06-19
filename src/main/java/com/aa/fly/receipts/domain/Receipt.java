/**
 *
 */
package com.aa.fly.receipts.domain;

/**
 * @author Shiva.Narendrula
 */
public class Receipt {
    private String firstName;
    private String lastName;
    private String receiptTotal;

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

    public String getReceiptTotal() {
        return receiptTotal;
    }

    public void setReceiptTotal(String receiptTotal) {
        this.receiptTotal = receiptTotal;
    }

    @Override
    public String toString() {
        return "Receipt{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", receiptTotal='"
                + receiptTotal + '\'' + '}';
    }
}
