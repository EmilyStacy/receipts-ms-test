package com.aa.fly.receipts.exception;

public enum StatusMessage {

    NO_COST("NoCostDetailsFound"), BULK_TICKET("BulkTicket");

    private String message;

    private StatusMessage(String message) {
        this.message = message;
    };

    public String getStatusMessage() {
        return this.message;
    };

}
