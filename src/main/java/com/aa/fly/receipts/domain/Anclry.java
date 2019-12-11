package com.aa.fly.receipts.domain;

import java.util.Objects;

public class Anclry {
    private String anclryDocNbr;
    private String anclryIssueDate;
    private String anclryProdCode;
    private String anclryProdName;
    private String anclryPriceCurrencyAmount;
    private String anclryPriceCurrencyCode;
    private String anclrySalesCurrencyAmount;
    private String anclrySalesCurrencyCode;
    private String anclryTaxCurrencyAmount;

    public Anclry() {

    }

    public Anclry(String anclryDocNbr, String anclryIssueDate, String anclryProdCode, String anclryProdName,
            String anclryPriceCurrencyAmount, String anclryPriceCurrencyCode, String anclrySalesCurrencyAmount,
            String anclrySalesCurrencyCode, String anclryTaxCurrencyAmount) {
        this.anclryDocNbr = anclryDocNbr;
        this.anclryIssueDate = anclryIssueDate;
        this.anclryProdCode = anclryProdCode;
        this.anclryProdName = anclryProdName;
        this.anclryPriceCurrencyAmount = anclryPriceCurrencyAmount;
        this.anclryPriceCurrencyCode = anclryPriceCurrencyCode;
        this.anclrySalesCurrencyAmount = anclrySalesCurrencyAmount;
        this.anclrySalesCurrencyCode = anclrySalesCurrencyCode;
        this.anclryTaxCurrencyAmount = anclryTaxCurrencyAmount;
    }

    public String getAnclryDocNbr() {
        return anclryDocNbr;
    }

    public void setAnclryDocNbr(String anclryDocNbr) {
        this.anclryDocNbr = anclryDocNbr;
    }

    public String getAnclryIssueDate() {
        return anclryIssueDate;
    }

    public void setAnclryIssueDate(String anclryIssueDate) {
        this.anclryIssueDate = anclryIssueDate;
    }

    public String getAnclryProdCode() {
        return anclryProdCode;
    }

    public void setAnclryProdCode(String anclryProdCode) {
        this.anclryProdCode = anclryProdCode;
    }

    public String getAnclryProdName() {
        return anclryProdName;
    }

    public void setAnclryProdName(String anclryProdName) {
        this.anclryProdName = anclryProdName;
    }

    public String getAnclryPriceCurrencyAmount() {
        return anclryPriceCurrencyAmount;
    }

    public void setAnclryPriceCurrencyAmount(String anclryPriceCurrencyAmount) {
        this.anclryPriceCurrencyAmount = anclryPriceCurrencyAmount;
    }

    public String getAnclryPriceCurrencyCode() {
        return anclryPriceCurrencyCode;
    }

    public void setAnclryPriceCurrencyCode(String anclryPriceCurrencyCode) {
        this.anclryPriceCurrencyCode = anclryPriceCurrencyCode;
    }

    public String getAnclrySalesCurrencyAmount() {
        return anclrySalesCurrencyAmount;
    }

    public void setAnclrySalesCurrencyAmount(String anclrySalesCurrencyAmount) {
        this.anclrySalesCurrencyAmount = anclrySalesCurrencyAmount;
    }

    public String getAnclrySalesCurrencyCode() {
        return anclrySalesCurrencyCode;
    }

    public void setAnclrySalesCurrencyCode(String anclrySalesCurrencyCode) {
        this.anclrySalesCurrencyCode = anclrySalesCurrencyCode;
    }

    public String getAnclryTaxCurrencyAmount() {
        return anclryTaxCurrencyAmount;
    }

    public void setAnclryTaxCurrencyAmount(String anclryTaxCurrencyAmount) {
        this.anclryTaxCurrencyAmount = anclryTaxCurrencyAmount;
    }

    @Override
    public String toString() {
        return "Anclry{" +
                "anclryDocNbr='" + anclryDocNbr + '\'' +
                ", anclryIssueDate='" + anclryIssueDate + '\'' +
                ", anclryProdCode='" + anclryProdCode + '\'' +
                ", anclryProdName='" + anclryProdName + '\'' +
                ", anclryPriceCurrencyAmount='" + anclryPriceCurrencyAmount + '\'' +
                ", anclryPriceCurrencyCode='" + anclryPriceCurrencyCode + '\'' +
                ", anclrySalesCurrencyAmount='" + anclrySalesCurrencyAmount + '\'' +
                ", anclrySalesCurrencyCode='" + anclrySalesCurrencyCode + '\'' +
                ", anclryTaxCurrencyAmount='" + anclryTaxCurrencyAmount + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Anclry anclry = (Anclry) o;
        return Objects.equals(anclryDocNbr, anclry.anclryDocNbr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anclryDocNbr);
    }
}
