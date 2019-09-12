package com.aa.fly.receipts.service.impl;

import com.aa.fly.receipts.data.WifiReceiptRepository;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.WifiReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class WifiReceiptServiceImpl implements WifiReceiptService {

    @Autowired
    private WifiReceiptRepository repository;

    @Override
    public WifiReceipt findWifiReceipt(WifiSearchCriteria criteria) {
        return repository.findWifiReceipt(criteria);
    }
}
