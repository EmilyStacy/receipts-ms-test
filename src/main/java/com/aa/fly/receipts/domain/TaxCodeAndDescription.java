package com.aa.fly.receipts.domain;

import java.util.Date;

public class TaxCodeAndDescription {
    private String taxCode;
    private String taxCodeDescription;
    private Date startDate;
    private Date endDate;

    public TaxCodeAndDescription(String taxCode, String taxCodeDescription, Date startDate, Date endDate) {
        this.taxCode = taxCode;
        this.taxCodeDescription = taxCodeDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxCodeDescription() {
        return taxCodeDescription;
    }

    public void setTaxCodeDescription(String taxCodeDescription) {
        this.taxCodeDescription = taxCodeDescription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override public String toString() {
        return "TaxCodeAndDescription{" +
                "taxCode='" + taxCode + '\'' +
                ", taxCodeDescription='" + taxCodeDescription + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
