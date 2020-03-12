package com.aa.fly.receipts.domain;

import java.util.Objects;

public class Tax {
    private String taxCodeSequenceId;
    private String taxCode;
    private String taxDescription;
    private String cityCode;
    private String taxAmount;
    private String taxCurrencyCode;

    public Tax() {

    }

    public Tax(String taxCodeSequenceId, String taxCode, String taxDescription, String cityCode, String taxAmount, String taxCurrencyCode) {
        this.taxCodeSequenceId = taxCodeSequenceId;
        this.taxCode = taxCode;
        this.taxDescription = taxDescription;
        this.cityCode = cityCode;
        this.taxAmount = taxAmount;
        this.taxCurrencyCode = taxCurrencyCode;
    }

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

    public Double getTaxAmountDouble() {
        Double tax = null;
        if (this.taxAmount != null) {
            tax = Double.valueOf(taxAmount);
        }
        return tax;
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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tax tax = (Tax) o;
        return Objects.equals(taxCodeSequenceId, tax.taxCodeSequenceId);
    }

    @Override public int hashCode() {
        return Objects.hash(taxCodeSequenceId);
    }

    @Override public String toString() {
        return "Tax{" +
                "taxCodeSequenceId='" + taxCodeSequenceId + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", taxDescription='" + taxDescription + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", taxAmount='" + taxAmount + '\'' +
                ", taxCurrencyCode='" + taxCurrencyCode + '\'' +
                '}';
    }
}
