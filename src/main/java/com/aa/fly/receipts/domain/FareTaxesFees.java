package com.aa.fly.receipts.domain;

public class FareTaxesFees {

    private String baseFareAmount;
    private String baseFareCurrencyCode;
    private String totalFareAmount;

    public FareTaxesFees() {
    }

    public FareTaxesFees(String baseFareAmount, String baseFareCurrencyCode, String totalFareAmount) {
        this.baseFareAmount = baseFareAmount;
        this.baseFareCurrencyCode = baseFareCurrencyCode;
        this.totalFareAmount = totalFareAmount;
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

    @Override
    public String toString() {
        return "FareTaxesFees{" +
                "baseFareAmount='" + baseFareAmount + '\'' +
                ", baseFareCurrencyCode='" + baseFareCurrencyCode + '\'' +
                ", totalFareAmount='" + totalFareAmount + '\'' +
                '}';
    }
}
