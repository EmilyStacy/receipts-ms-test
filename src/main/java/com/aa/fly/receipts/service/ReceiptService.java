package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import org.springframework.stereotype.Service;

/**
 * Created by 629874 on 5/9/2019.
 */
public interface ReceiptService
{
   public Receipt findReceipt(SearchCriteria criteria);
}
