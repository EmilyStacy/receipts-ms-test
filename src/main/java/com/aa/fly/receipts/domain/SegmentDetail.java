package com.aa.fly.receipts.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SegmentDetail
{
    private String segmentDepartureAirportName;
    private String segmentArrivalAirportName;
    private String segmentDepartureAirportCode;
    private String segmentArrivalAirportCode;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date segmentDepartureDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private Date segmentDepartureTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private Date segmentArrivalTime;

    private String carrierCode;
    private String flightNumber;
    private String bookingClass;
    private String fareBasis;

    public String getSegmentDepartureAirportName() {
        return segmentDepartureAirportName;
    }

    public void setSegmentDepartureAirportName(String segmentDepartureAirportName) {
        this.segmentDepartureAirportName = segmentDepartureAirportName;
    }

    public String getSegmentArrivalAirportName() {
        return segmentArrivalAirportName;
    }

    public void setSegmentArrivalAirportName(String segmentArrivalAirportName) {
        this.segmentArrivalAirportName = segmentArrivalAirportName;
    }

    public String getSegmentDepartureAirportCode() {
        return segmentDepartureAirportCode;
    }

    public void setSegmentDepartureAirportCode(String segmentDepartureAirportCode) {
        this.segmentDepartureAirportCode = segmentDepartureAirportCode;
    }

    public String getSegmentArrivalAirportCode() {
        return segmentArrivalAirportCode;
    }

    public void setSegmentArrivalAirportCode(String segmentArrivalAirportCode) {
        this.segmentArrivalAirportCode = segmentArrivalAirportCode;
    }

    public Date getSegmentDepartureDate() {
        return segmentDepartureDate;
    }

    public void setSegmentDepartureDate(Date segmentDepartureDate) {
        this.segmentDepartureDate = segmentDepartureDate;
    }

    public Date getSegmentDepartureTime() {
        return segmentDepartureTime;
    }

    public void setSegmentDepartureTime(Date segmentDepartureTime) {
        this.segmentDepartureTime = segmentDepartureTime;
    }

    public Date getSegmentArrivalTime() {
        return segmentArrivalTime;
    }

    public void setSegmentArrivalTime(Date segmentArrivalTime) {
        this.segmentArrivalTime = segmentArrivalTime;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

    public String getFareBasis() {
        return fareBasis;
    }

    public void setFareBasis(String fareBasis) {
        this.fareBasis = fareBasis;
    }

    @Override public String toString() {
        return "SegmentDetail{" +
                "segmentDepartureAirportName='" + segmentDepartureAirportName + '\'' +
                ", segmentArrivalAirportName='" + segmentArrivalAirportName + '\'' +
                ", segmentDepartureAirportCode='" + segmentDepartureAirportCode + '\'' +
                ", segmentArrivalAirportCode='" + segmentArrivalAirportCode + '\'' +
                ", segmentDepartureDate=" + segmentDepartureDate +
                ", segmentDepartureTime=" + segmentDepartureTime +
                ", segmentArrivalTime=" + segmentArrivalTime +
                ", carrierCode='" + carrierCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", bookingClass='" + bookingClass + '\'' +
                ", fareBasis='" + fareBasis + '\'' +
                '}';
    }
}