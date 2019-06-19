package com.aa.fly.receipts.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.aa.fly.receipts.controller.ReceiptControllerTest;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

@RunWith(SpringRunner.class)
@DirtiesContext
public class ReceiptsMSDomainTest {

    private static final Validator ACCESSOR_VALIDATOR = ValidatorBuilder.create().with(new GetterTester())
            .with(new SetterTester()).build();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static void validateAccessors(final Class<?> clazz) {
        ACCESSOR_VALIDATOR.validate(PojoClassFactory.getPojoClass(clazz));
    }

    @Test
    public void testAccesors_shouldAccessProperField() {

        validateAccessors(SearchCriteria.class);
        validateAccessors(Receipt.class);
        validateAccessors(WifiReceipt.class);
        validateAccessors(WifiSearchCriteria.class);
        validateAccessors(WifiLineItem.class);

    }

    @Test
    public void testWifiSearchCriteriaToString() throws ParseException {
        WifiSearchCriteria criteria = getWifiSearchCriteria();
        Assert.assertEquals(
                "WifiSearchCriteria{ccLastFour='null', lastName='smith', startDate='2017-01-01', endDate='2019-06-01'}",
                criteria.toString());
    }

    @Test
    public void testReceiptToString() {
        SearchCriteria criteria = ReceiptControllerTest.getSearchCriteria();
        Receipt receipt = ReceiptControllerTest.getReceipt(criteria);
        Assert.assertEquals("Receipt{firstName='first', lastName='last', receiptTotal='239.00'}", receipt.toString());
    }

    @Test
    public void testWifiReceiptToString() throws ParseException {
        WifiReceipt receipt = getWifiReceipt();
        Assert.assertEquals(
                "WifiReceipt{WifiLineItem{orderId='123', purchaseDate='2019-06-01', productName='Wifi Monthly Subscription', productPrice='10.00', currencyCode='USD', taxAmount='1.00', netPrice='11.00', ccLastFour='1234', ccTypeCode='MC', lastName='last'}}",
                receipt.toString());
    }

    public static WifiSearchCriteria getWifiSearchCriteria() throws ParseException {
        WifiSearchCriteria criteria = new WifiSearchCriteria();
        criteria.setLastName("smith");
        criteria.setStartDate(dateFormat.parse("01/01/2017"));
        criteria.setEndDate(dateFormat.parse("06/01/2019"));
        return criteria;
    }

    public static WifiReceipt getWifiReceipt() throws ParseException {
        WifiReceipt wifiReceipt = new WifiReceipt();
        WifiLineItem lineItem = new WifiLineItem();
        lineItem.setCcLastFour("1234");
        lineItem.setCcTypeCode("MC");
        lineItem.setCurrencyCode("USD");
        lineItem.setLastName("last");
        lineItem.setNetPrice("11.00");
        lineItem.setOrderId("123");
        lineItem.setProductName("Wifi Monthly Subscription");
        lineItem.setProductPrice("10.00");
        lineItem.setPurchaseDate(dateFormat.parse("06/01/2019"));
        lineItem.setTaxAmount("1.00");
        wifiReceipt.setWifiLineItems(new ArrayList<>());
        wifiReceipt.getWifiLineItems().add(lineItem);
        return wifiReceipt;
    }

}
