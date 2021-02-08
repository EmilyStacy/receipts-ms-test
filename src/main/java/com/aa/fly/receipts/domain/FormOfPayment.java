package com.aa.fly.receipts.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

public class FormOfPayment {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fopIssueDate;
    private String fopTypeCode;
    private String fopTypeDescription;
    private String fopAccountNumberLast4;
    private String fopAmount;
    private String fopCurrencyCode;
    private boolean isTicket = false;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Set<Ancillary> ancillaries = new HashSet<>();

    public FormOfPayment() {

    }

    public FormOfPayment(Date fopIssueDate, String fopTypeCode, String fopAccountNumberLast4, String fopAmount, String fopCurrencyCode) {
        this.fopIssueDate = fopIssueDate;
        this.fopTypeCode = fopTypeCode;
        this.fopAccountNumberLast4 = fopAccountNumberLast4;
        this.fopAmount = fopAmount;
        this.fopCurrencyCode = fopCurrencyCode;
    }

    public boolean isTicket() {
        return isTicket;
    }

    public void setIsTicket(boolean isTicket) {
        this.isTicket = isTicket;
    }

    public Date getFopIssueDate() {
        return fopIssueDate;
    }

    public void setFopIssueDate(Date fopIssueDate) {
        this.fopIssueDate = fopIssueDate;
    }

    public String getFopTypeCode() {
        return fopTypeCode;
    }

    public void setFopTypeCode(String fopTypeCode) {
        this.fopTypeCode = fopTypeCode;
    }

    public String getFopTypeDescription() {
        return fopTypeDescription;
    }

    public void setFopTypeDescription(String fopTypeDescription) {
        this.fopTypeDescription = fopTypeDescription;
    }

    public String getFopAmount() {
        return fopAmount;
    }

    public void setFopAmount(String fopAmount) {
        this.fopAmount = fopAmount;
    }

    public String getFopAccountNumberLast4() {
        return fopAccountNumberLast4;
    }

    public void setFopAccountNumberLast4(String fopAccountNumberLast4) {
        this.fopAccountNumberLast4 = fopAccountNumberLast4;
    }

    public String getFopCurrencyCode() {
        return fopCurrencyCode;
    }

    public void setFopCurrencyCode(String fopCurrencyCode) {
        this.fopCurrencyCode = fopCurrencyCode;
    }

    public Set<Ancillary> getAncillaries() {
        return ancillaries;
    }

    public void setAncillaries(Set<Ancillary> ancillaries) {
        this.ancillaries = ancillaries;
    }

    @Override
    public String toString() {
        return "FormOfPayment{" +
                "fopIssueDate=" + dateFormat.format(fopIssueDate) +
                ", fopTypeCode='" + fopTypeCode + '\'' +
                ", fopTypeDescription='" + fopTypeDescription + '\'' +
                ", fopAccountNumberLast4='" + fopAccountNumberLast4 + '\'' +
                ", fopAmount='" + fopAmount + '\'' +
                ", fopCurrencyCode='" + fopCurrencyCode + '\'' +
                ", ancillaries='" + ancillaries + '\'' +
                '}';
    }
}
