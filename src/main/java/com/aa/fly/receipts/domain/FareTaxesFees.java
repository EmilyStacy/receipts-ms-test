package com.aa.fly.receipts.domain;

import java.util.HashSet;
import java.util.Set;

public class FareTaxesFees {

    private String baseFareAmount;
    private String baseFareCurrencyCode;
    private String totalFareAmount;
    private String taxFareAmount;
    private Set<Tax> taxes = new HashSet<>();

    public FareTaxesFees() {
    }

    public FareTaxesFees(String baseFareAmount, String baseFareCurrencyCode, String totalFareAmount, String taxFareAmount) {
        this.baseFareAmount = baseFareAmount;
        this.baseFareCurrencyCode = baseFareCurrencyCode;
        this.totalFareAmount = totalFareAmount;
        this.taxFareAmount = taxFareAmount;
    }

    public String getBaseFareAmount() {
        return baseFareAmount;
    }

    public void setBaseFareAmount(String baseFareAmount) {
        this.baseFareAmount = baseFareAmount;
    }

    public String getBaseFareCurrencyCode() {
        return baseFareCurrencyCode;
    }

    public void setBaseFareCurrencyCode(String baseFareCurrencyCode) {
        this.baseFareCurrencyCode = baseFareCurrencyCode;
    }

    public String getTotalFareAmount() {
        return totalFareAmount;
    }

    public void setTotalFareAmount(String totalFareAmount) {
        this.totalFareAmount = totalFareAmount;
    }

    public String getTaxFareAmount() {
        return taxFareAmount;
    }

    public void setTaxFareAmount(String taxFareAmount) {
        this.taxFareAmount = taxFareAmount;
    }

    public Set<Tax> getTaxes() {
        return taxes;
    }

    public void setTaxes(Set<Tax> taxes) {
        this.taxes = taxes;
    }

    @Override
    public String toString() {
        return "FareTaxesFees{" +
                "baseFareAmount='" + baseFareAmount + '\'' +
                ", baseFareCurrencyCode='" + baseFareCurrencyCode + '\'' +
                ", totalFareAmount='" + totalFareAmount + '\'' +
                ", taxFareAmount='" + taxFareAmount + '\'' +
                ", taxes='" + taxes + '\'' +
                '}';
    }
}
