/**
 * 
 */
package com.aa.fly.receipts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aa.ct.fly.logging.annotations.MSLogger;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.BulkTicketException;
import com.aa.fly.receipts.exception.StatusMessage;
import com.aa.fly.receipts.service.TicketReceiptService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author 629874
 */

@RestController
@RequestMapping("/api")
public class TicketReceiptController {

    @Autowired
    private TicketReceiptService ticketReceiptService;

    @MSLogger
    @ApiOperation(value = "Find ticket receipt by ticketNumber, pnr, firstName, lastName, departureDate")
    @ApiResponses({ @ApiResponse(
            code = 500, // HttpStatus.INTERNAL_SERVER_ERROR
            message = "Unexpected Error",
            response = TicketReceipt.class) })
    @PostMapping("/ticket-receipt")
    public ResponseEntity<TicketReceipt> getTicketReceipt(@RequestBody SearchCriteria searchCriteria)

    {
        return ticketReceiptService.findTicketReceipt(searchCriteria);
    }

    @ExceptionHandler({ BulkTicketException.class})
    @ResponseStatus(HttpStatus.OK)
    public final TicketReceipt throwBulkTicketException(BulkTicketException bulkTicket) {
        TicketReceipt ticketReceipt = new TicketReceipt();

        ticketReceipt.setStatusMessage(StatusMessage.BULK_TICKET.getStatusMessage());

        return ticketReceipt;
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
