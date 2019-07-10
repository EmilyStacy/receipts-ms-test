/**
 * 
 */
package com.aa.fly.receipts.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aa.etds.logging.log4j2.mdc.LoggingContextValues;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.ReceiptService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author 629874
 */

@RestController
@RequestMapping("/api")
public class ReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

    @Autowired
    private ReceiptService receiptService;

    @ApiOperation(value = "Find receipt by ticketNumber, firstName, lastName, departureDate")
    @ApiResponses({@ApiResponse(
            code = 500, // HttpStatus.INTERNAL_SERVER_ERROR
            message = "Unexpected Error",
            response = Receipt.class)})
    @PostMapping("/receipt")
    public Receipt getReceipts(@RequestBody SearchCriteria searchCriteria)

    {

        String trasactionId = UUID.randomUUID().toString();
        LoggingContextValues.updateMDC(trasactionId, "NONE", "ReceiptsMS");

        logger.info("*************************** find Receipt ************************* {}", searchCriteria);

        return receiptService.findReceipt(searchCriteria);

    }

    @ApiOperation(value = "Find wifi receipt by ccLastFour, lastName, date range")
    @ApiResponses({@ApiResponse(
            code = 500, // HttpStatus.INTERNAL_SERVER_ERROR
            message = "Unexpected Error",
            response = WifiReceipt.class)})
    @PostMapping("/wifi-receipt")
    public WifiReceipt getWifiReceipts(@RequestBody WifiSearchCriteria wifiSearchCriteria)

    {

        String trasactionId = UUID.randomUUID().toString();
        LoggingContextValues.updateMDC(trasactionId, "NONE", "ReceiptsMS");

        logger.info("*************************** find Receipt ************************* {}",
                wifiSearchCriteria.toString());

        return receiptService.findWifiReceipt(wifiSearchCriteria);

    }

}