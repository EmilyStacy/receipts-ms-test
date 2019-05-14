package com.aa.fly.receipts.service.impl;

import com.aa.fly.receipts.data.ReceiptRepository;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class ReceiptServiceImpl implements ReceiptService
{

   @Autowired
   private ReceiptRepository repository;


   public Receipt findReceipt(SearchCriteria criteria)
   {
      return repository.findReceipt(criteria);
   }
}
