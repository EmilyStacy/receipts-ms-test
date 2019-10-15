package com.aa.fly.receipts.domain;

import java.util.ArrayList;
import java.util.List;

public class AirportLookup {
    
    private String fieldErrors;
    private String presentationErrors;
    private String infoMessages;
    private String alertMessage;
    private String messageParams;
    private List<Airport> airportList = new ArrayList<>();

    public String getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(String fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public String getPresentationErrors() {
        return presentationErrors;
    }

    public void setPresentationErrors(String presentationErrors) {
        this.presentationErrors = presentationErrors;
    }

    public String getInfoMessages() {
        return infoMessages;
    }

    public void setInfoMessages(String infoMessages) {
        this.infoMessages = infoMessages;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getMessageParams() {
        return messageParams;
    }

    public void setMessageParams(String messageParams) {
        this.messageParams = messageParams;
    }

    public List<Airport> getAirportList() {
        return airportList;
    }

    public void setAirportList(List<Airport> airportList) {
        this.airportList = airportList;
    }

    @Override
    public String toString() {
        return "AirportLookup{" +
                "fieldErrors='" + fieldErrors + '\'' +
                ", presentationErrors='" + presentationErrors + '\'' +
                ", infoMessages='" + infoMessages + '\'' +
                ", alertMessage='" + alertMessage + '\'' +
                ", messageParams='" + messageParams + '\'' +
                ", airportList=" + airportList +
                '}';
    }
}
