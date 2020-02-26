package com.aa.fly.receipts.exception;

public class NoCostDetailsFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public NoCostDetailsFoundException(String message) {
        super(message);
    }
}
