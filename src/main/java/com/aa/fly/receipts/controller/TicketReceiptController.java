/**
 * 
 */
package com.aa.fly.receipts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aa.ct.fly.logging.annotations.MSLogger;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketSummary;
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
    @ApiOperation(value = "Find ticket summary by ticketNumber, pnr, firstName, lastName, departureDate")
    @ApiResponses({ @ApiResponse(
            code = 500, // HttpStatus.INTERNAL_SERVER_ERROR
            message = "Unexpected Error",
            response = Receipt.class) })
    @PostMapping("/ticket-summary")
    public TicketSummary getTicketSummary(@RequestBody SearchCriteria searchCriteria)

    {
        return ticketReceiptService.findTicketSummary(searchCriteria);
    }

}
