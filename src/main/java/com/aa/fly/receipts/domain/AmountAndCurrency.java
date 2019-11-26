package com.aa.fly.receipts.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountAndCurrency {
    private String amount;
    private String currencyCode;

    private Pattern endsWithDigit = Pattern.compile("(.*)(\\d)$");
    public AmountAndCurrency(String amount, String currencyCode) {

        Matcher matcher = endsWithDigit.matcher(currencyCode);

        if(amount == null || currencyCode == null || !matcher.matches()) {
            this.currencyCode = currencyCode;
            this.amount = amount;

        } else {
            String numberOfDecimals = matcher.group(2);
            this.currencyCode = matcher.group(1);
            this.amount = new BigDecimal(amount).divide(BigDecimal.valueOf(10).pow(Integer.parseInt(numberOfDecimals))).setScale(2, RoundingMode.CEILING).toString();
        }
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override public String toString() {
        return "AmountAndCurrency{" +
                "amount='" + amount + '\'' +
                ", currency='" + currencyCode + '\'' +
                '}';
    }
}
