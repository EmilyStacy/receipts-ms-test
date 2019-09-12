package com.aa.fly.receipts.service;

import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;

/**
 * Created by 629874 on 5/9/2019.
 */
@SuppressWarnings("squid:S1609")
public interface WifiReceiptService {
    /**
     * Find the wifi receipt data for given search criteria
     * 
     * @param criteria - wifi search criteria
     * @return - instance of WifiReceipt
     */
    public WifiReceipt findWifiReceipt(WifiSearchCriteria criteria);
}
