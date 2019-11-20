/**
 *
 */
package com.aa.fly.receipts.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Shiva.Narendrula
 */
public class WifiLineItem {
    private String orderId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date purchaseDate;
    private String productName;
    private String productPrice;
    private String currencyCode;
    private String taxAmount;
    private String netPrice;
    private String ccLastFour;
    private String ccTypeCode;
    private String ccTypeName;
    private String lastName;
    private String seller;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(String netPrice) {
        this.netPrice = netPrice;
    }

    public String getCcLastFour() {
        return ccLastFour;
    }

    public void setCcLastFour(String ccLastFour) {
        this.ccLastFour = ccLastFour;
    }

    public String getCcTypeCode() {
        return ccTypeCode;
    }

    public void setCcTypeCode(String ccTypeCode) {
        this.ccTypeCode = ccTypeCode;
    }

    public String getCcTypeName() {
        return ccTypeName;
    }

    public void setCcTypeName(String ccTypeName) {
        this.ccTypeName = ccTypeName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "WifiLineItem{" + "orderId='" + orderId + '\'' + ", purchaseDate='" + dateFormat.format(purchaseDate)
                + '\'' + ", productName='" + productName + '\'' + ", productPrice='" + productPrice + '\''
                + ", currencyCode='" + currencyCode + '\'' + ", taxAmount='" + taxAmount + '\'' + ", netPrice='"
                + netPrice + '\'' + ", ccLastFour='" + ccLastFour + '\'' + ", ccTypeCode='" + ccTypeCode + '\'' + ", ccTypeName='" + ccTypeName + '\''
                + ", lastName='" + lastName + '\'' + '}';
    }
}
