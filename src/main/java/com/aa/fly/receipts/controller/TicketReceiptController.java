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
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
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
    public TicketReceipt getTicketReceipt(@RequestBody SearchCriteria searchCriteria)

    {
        return ticketReceiptService.findTicketReceipt(searchCriteria);
    }

}
