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
public class WifiSearchCriteria {

    private String ccLastFour;
    private String lastName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getCcLastFour() {
        return ccLastFour;
    }

    public void setCcLastFour(String ccLastFour) {
        this.ccLastFour = ccLastFour;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date fromDate) {
        this.startDate = fromDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "WifiSearchCriteria{" + "ccLastFour='" + ccLastFour + '\'' + ", lastName='" + lastName + '\''
                + ", startDate='" + dateFormat.format(startDate) + '\'' + ", endDate='" + dateFormat.format(endDate)
                + '\'' + '}';
    }
}
