/**
 * 
 */
package com.aa.fly.receipts.controller;

import java.util.UUID;

import com.aa.fly.receipts.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aa.etds.logging.log4j2.mdc.LoggingContextValues;

/**
 * @author 629874
 *
 */

@RestController
@RequestMapping( "/api" )
public class ReceiptController
{

   private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class );

   @Autowired
   private ReceiptService receiptService;

   @PostMapping("/receipt")
   public Receipt getReceipts(@RequestBody SearchCriteria searchCriteria)

   {

      String trasactionId = UUID.randomUUID()
                                .toString();
      LoggingContextValues.updateMDC( trasactionId,
                                      ( "NONE" ),
                                      "ReceiptsMS" );

      logger.info( "*************************** find Receipt ************************* {}",
                   searchCriteria );

      return receiptService.findReceipt(searchCriteria);

   }


}
