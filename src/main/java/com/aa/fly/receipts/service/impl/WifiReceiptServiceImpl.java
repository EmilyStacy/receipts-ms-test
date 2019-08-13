package com.aa.fly.receipts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aa.fly.receipts.data.WifiReceiptRepository;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.WifiReceiptService;

/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class WifiReceiptServiceImpl implements WifiReceiptService {

    @Autowired
    private WifiReceiptRepository repository;

    @Override
    public Receipt findReceipt(SearchCriteria criteria) {
        return repository.findReceipt(criteria);
    }

    @Override
    public WifiReceipt findWifiReceipt(WifiSearchCriteria criteria) {
        return repository.findWifiReceipt(criteria);
    }
}
