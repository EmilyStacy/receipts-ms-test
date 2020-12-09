package com.aa.fly.receipts.controller;

import com.aa.ct.fly.logging.annotations.MSLogger;
import com.aa.fly.receipts.domain.SearchCriteriaApi2;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.TicketReceiptApi2Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api2")
public class TicketReceiptApi2Controller {

    @Autowired
    private TicketReceiptApi2Service ticketReceiptApi2Service;

    @MSLogger
    @ApiOperation(value = "Find ticket receipt by ticketNumber, lastName")
    @ApiResponses({ @ApiResponse(
            code = 500, // HttpStatus.INTERNAL_SERVER_ERROR
            message = "Unexpected Error",
            response = TicketReceipt.class) })
    @PostMapping("/ticket-receipt")
    public ResponseEntity<TicketReceipt> getTicketReceipt(@RequestBody SearchCriteriaApi2 searchCriteriaApi2)

    {
        return ticketReceiptApi2Service.findTicketReceipt(searchCriteriaApi2);
    }

    @ExceptionHandler({ UncategorizedSQLException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final TicketReceipt uncategorizedSQLException(UncategorizedSQLException ex) {
        TicketReceipt ticketReceipt = new TicketReceipt();
        if(ex.getMessage() != null && ex.getMessage().contains("SocketTimeoutException")) {
            ticketReceipt.setStatusMessage("QueryTimeout");
        } else {
            ticketReceipt.setStatusMessage("UncategorizedSQLException");
        }

        return ticketReceipt;
    }
}
