/**
 * 
 */
package com.aa.fly.receipts.controller;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.aa.fly.receipts.domain.ReceiptResponse;
import com.aa.fly.receipts.domain.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aa.etds.logging.log4j2.mdc.LoggingContextValues;

/**
 * @author 629874
 *
 */

@RestController
@RequestMapping( "/api" )
public class ReceiptsController
{

   private static final Logger logger = LoggerFactory.getLogger( MethodHandles.lookup()
                                                                              .lookupClass() );

   //@RequestMapping( value = "/receipt", method = RequestMethod.POST )
   @PostMapping("/receipt")
   public ReceiptResponse getReceipts(@RequestBody SearchCriteria searchCriteria)

   {

      String trasactionId = UUID.randomUUID()
                                .toString();
      LoggingContextValues.updateMDC( trasactionId,
                                      ( "NONE" ),
                                      "ReceiptsMS" );

      logger.info( "*************************** Transaction Id ************************* {}",
                   trasactionId );

      ReceiptResponse response = new ReceiptResponse();
      response.setFirstName(searchCriteria.getFirstName());
      response.setLastName(searchCriteria.getLastName());
      response.setReceiptTotal("$239.00");
      return response;

   }


}
