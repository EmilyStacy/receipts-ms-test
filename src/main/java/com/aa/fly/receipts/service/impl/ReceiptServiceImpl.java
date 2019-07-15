package com.aa.fly.receipts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aa.fly.receipts.data.ReceiptRepository;
import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;
import com.aa.fly.receipts.service.ReceiptService;

/**
 * Created by 629874 on 5/9/2019.
 */
@Service

public class ReceiptServiceImpl implements ReceiptService {

    @Autowired
    private ReceiptRepository repository;

    @Override
    public Receipt findReceipt(SearchCriteria criteria) {
        return repository.findReceipt(criteria);
    }

    @Override
    public WifiReceipt findWifiReceipt(WifiSearchCriteria criteria) {
        return repository.findWifiReceipt(criteria);
    }
}
