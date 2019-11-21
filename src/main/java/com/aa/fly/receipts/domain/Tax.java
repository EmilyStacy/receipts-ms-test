package com.aa.fly.receipts.domain;

import java.util.Objects;

public class Tax {
    private String taxCodeSequenceId;
    private String taxCode;
    private String taxDescription;
    private String taxAmount;
    private String taxCurrencyCode;

    public String getTaxCodeSequenceId() {
        return taxCodeSequenceId;
    }

    public void setTaxCodeSequenceId(String taxCodeSequenceId) {
        this.taxCodeSequenceId = taxCodeSequenceId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxCurrencyCode() {
        return taxCurrencyCode;
    }

    public void setTaxCurrencyCode(String taxCurrencyCode) {
        this.taxCurrencyCode = taxCurrencyCode;
    }

    @Override public String toString() {
        return "Tax{" +
                "taxCodeSequenceId='" + taxCodeSequenceId + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", taxDescription='" + taxDescription + '\'' +
                ", taxAmount='" + taxAmount + '\'' +
                ", taxCurrencyCode='" + taxCurrencyCode + '\'' +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tax tax = (Tax) o;
        return Objects.equals(taxCode, tax.taxCode);
    }

    @Override public int hashCode() {
        return Objects.hash(taxCode);
    }
}
