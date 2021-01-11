package com.aa.fly.receipts.exception;

public enum StatusMessage {

    BULK_TICKET("BulkTicket"),
    AGENCY_TICKET("AgencyTicket");

    private String message;

    private StatusMessage(String message) {
        this.message = message;
    };

    public String getStatusMessage() {
        return this.message;
    };

}
