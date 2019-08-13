package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;

/**
 * Created by 629874 on 5/9/2019.
 */
public interface WifiReceiptService {
    public Receipt findReceipt(SearchCriteria criteria);

    /**
     * Find the wifi receipt data for given search criteria
     * 
     * @param criteria - wifi search criteria
     * @return - instance of WifiReceipt
     */
    public WifiReceipt findWifiReceipt(WifiSearchCriteria criteria);
}
