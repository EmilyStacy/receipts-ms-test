package com.aa.fly.receipts.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SegmentDetail {
    private Airport departureAirport;
    private Airport arrivalAirport;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date segmentDepartureDate;
    private String segmentDepartureTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date segmentArrivalDate;
    private String segmentArrivalTime;

    private String carrierCode;
    private String flightNumber;
    private String bookingClass;
    private String fareBasis;
    private String returnTrip;
    private String segmentStatus;

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Date getSegmentDepartureDate() {
        return segmentDepartureDate;
    }

    public void setSegmentDepartureDate(Date segmentDepartureDate) {
        this.segmentDepartureDate = segmentDepartureDate;
    }

    public String getSegmentDepartureTime() {
        return segmentDepartureTime;
    }

    public void setSegmentDepartureTime(String segmentDepartureTime) {
        this.segmentDepartureTime = segmentDepartureTime;
    }

    public Date getSegmentArrivalDate() {
        return segmentArrivalDate;
    }

    public void setSegmentArrivalDate(Date segmentArrivalDate) {
        this.segmentArrivalDate = segmentArrivalDate;
    }

    public String getSegmentArrivalTime() {
        return segmentArrivalTime;
    }

    public void setSegmentArrivalTime(String segmentArrivalTime) {
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

    public String getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(String returnTrip) {
        this.returnTrip = returnTrip;
    }

    public void setSegmentStatus(String segmentStatus) {
        this.segmentStatus = segmentStatus;
    }

    public String getSegmentStatus() {
        return segmentStatus;
    }

    @Override public String toString() {
        return "SegmentDetail{" +
                "departureAirport=" + departureAirport +
                ", arrivalAirport=" + arrivalAirport +
                ", segmentDepartureDate=" + segmentDepartureDate +
                ", segmentDepartureTime='" + segmentDepartureTime + '\'' +
                ", segmentArrivalDate=" + segmentArrivalDate +
                ", segmentArrivalTime='" + segmentArrivalTime + '\'' +
                ", carrierCode='" + carrierCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", bookingClass='" + bookingClass + '\'' +
                ", fareBasis='" + fareBasis + '\'' +
                ", returnTrip='" + returnTrip + '\'' +
                ", segmentStatus='" + segmentStatus + '\'' +
                '}';
    }
}
