package com.aa.fly.receipts.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleRuntimeException(Exception ex, WebRequest request) {
        logger.error("{} occured while trying to retrieve receipt", ex.getClass().getName());
        logger.error("Error details = ",ex);
        return new ResponseEntity<>("Internal error occured", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
