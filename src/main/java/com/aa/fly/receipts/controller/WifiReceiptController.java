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
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.WifiReceiptService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author 629874
 */

@RestController
@RequestMapping("/api")
public class WifiReceiptController {

    @Autowired
    private WifiReceiptService wifiReceiptService;

    @MSLogger
    @ApiOperation(value = "Find wifi receipt by ccLastFour, lastName, date range")
    @ApiResponses({ @ApiResponse(
            code = 500, // HttpStatus.INTERNAL_SERVER_ERROR
            message = "Unexpected Error",
            response = WifiReceipt.class) })
    @PostMapping("/wifi-receipt")
    public WifiReceipt getWifiReceipts(@RequestBody WifiSearchCriteria wifiSearchCriteria)

    {
        return wifiReceiptService.findWifiReceipt(wifiSearchCriteria);
    }
}
