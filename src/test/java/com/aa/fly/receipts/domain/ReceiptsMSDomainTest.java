package com.aa.fly.receipts.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void testAccesorShouldAccessProperField() {
        validateAccessors(SearchCriteria.class);
        validateAccessors(WifiReceipt.class);
        validateAccessors(WifiSearchCriteria.class);
        validateAccessors(WifiLineItem.class);
        validateAccessors(TicketReceipt.class);
        validateAccessors(PassengerDetail.class);
        validateAccessors(SegmentDetail.class);
        validateAccessors(Airport.class);
        validateAccessors(AirportLookup.class);
        validateAccessors(AirportLookupObject.class);
        validateAccessors(FormOfPayment.class);
        validateAccessors(FareTaxesFees.class);
        validateAccessors(Ancillary.class);
    }

    @Test
    public void testWifiSearchCriteriaToString() throws ParseException {
        WifiSearchCriteria criteria = getWifiSearchCriteria();
        Assert.assertEquals(
                "WifiSearchCriteria{ccLastFour='1234', lastName='smith', startDate='2017-01-01', endDate='2019-06-01'}",
                criteria.toString());
    }

    @Test
    public void testWifiReceiptToString() throws ParseException {
        WifiReceipt receipt = getWifiReceipt();
        Assert.assertEquals(
                "WifiReceipt{WifiLineItem{orderId='123', purchaseDate='2019-06-01', productName='Wifi Monthly Subscription', productPrice='10.00', currencyCode='USD', taxAmount='1.00', netPrice='11.00', ccLastFour='1234', ccTypeCode='MC', ccTypeName='MASTERCARD', lastName='last'}}",
                receipt.toString());
    }

    @Test
    public void testTicketSummaryToString() throws ParseException {
        TicketReceipt ticketReceipt = getTicketReceipt();
        Assert.assertEquals(
                "TicketSummary [airlineAccountCode=001, ticketIssueDate=2019-03-14, departureDate=2019-09-30, originAirport=Airport{code='MCO', name='Orlando International', stateCode='FL', city='Orlando', countryCode='USA', countryName='United States}, destinationAirport=Airport{code='MIA', name='Miami International', stateCode='FL', city='Miami', countryCode='USA', countryName='United States}, pnr=MRYMPT, dateFormat=java.text.SimpleDateFormat@f67a0200, passengerDetails=[ticketNumber=2371661425, firstName=SIMON, lastName=TEST, advantageNumber=XYZ1234, passengerTotalAmount=123.45, fareTaxesFees=FareTaxesFees{baseFareAmount='77674', baseFareCurrencyCode='USD2', totalFareAmount='84930', taxFareAmount='7256', taxes='[]'}, loyaltyOwnerCode=AA, formOfPayments=[FormOfPayment{fopIssueDate=2019-03-14, fopTypeCode='CCBA', fopTypeDescription='null', fopAccountNumberLast4='0006', fopAmount='225295', fopCurrencyCode='USD2', ancillaries='[Anclry{anclryDocNbr='654200213', anclryIssueDate='2019-11-07', anclryProdCode='090', anclryProdName='MAIN CABIN EXTRA (DFW - BDL)', anclryPriceCurrencyAmount='72.91', anclryPriceCurrencyCode='USD', anclrySalesCurrencyAmount='78.38', anclrySalesCurrencyCode='USD', anclryTaxCurrencyAmount='5.47'}]'}]], segmentDetails=[]]",
                ticketReceipt.toString());
    }

    @Test
    public void testPassengerDetailToString() throws ParseException {
        TicketReceipt ticketReceipt = getTicketReceipt();
        Assert.assertEquals(
                "ticketNumber=2371661425, firstName=SIMON, lastName=TEST, advantageNumber=XYZ1234, passengerTotalAmount=123.45, fareTaxesFees=FareTaxesFees{baseFareAmount='77674', baseFareCurrencyCode='USD2', totalFareAmount='84930', taxFareAmount='7256', taxes='[]'}, loyaltyOwnerCode=AA, formOfPayments=[FormOfPayment{fopIssueDate=2019-03-14, fopTypeCode='CCBA', fopTypeDescription='null', fopAccountNumberLast4='0006', fopAmount='225295', fopCurrencyCode='USD2', ancillaries='[Anclry{anclryDocNbr='654200213', anclryIssueDate='2019-11-07', anclryProdCode='090', anclryProdName='MAIN CABIN EXTRA (DFW - BDL)', anclryPriceCurrencyAmount='72.91', anclryPriceCurrencyCode='USD', anclrySalesCurrencyAmount='78.38', anclrySalesCurrencyCode='USD', anclryTaxCurrencyAmount='5.47'}]'}]",
                ticketReceipt.getPassengerDetails().get(0).toString());
    }

    public static WifiSearchCriteria getWifiSearchCriteria() throws ParseException {
        WifiSearchCriteria criteria = new WifiSearchCriteria();
        criteria.setLastName("smith");
        criteria.setCcLastFour("1234");
        criteria.setStartDate(dateFormat.parse("01/01/2017"));
        criteria.setEndDate(dateFormat.parse("06/01/2019"));
        return criteria;
    }

    public static SearchCriteria getSearchCriteriaWithTicketNumber() throws ParseException {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setLastName("TEST");
        criteria.setFirstName("SIMON");
        criteria.setTicketNumber("0012372187652");
        criteria.setDepartureDate(dateFormat.parse("09/30/2019"));
        return criteria;
    }

    public static WifiReceipt getWifiReceipt() throws ParseException {
        WifiReceipt wifiReceipt = new WifiReceipt();
        WifiLineItem lineItem = new WifiLineItem();
        lineItem.setCcLastFour("1234");
        lineItem.setCcTypeCode("MC");
        lineItem.setCcTypeName("MASTERCARD");
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

    public static TicketReceipt getTicketReceipt() throws ParseException {
        TicketReceipt ticketReceipt = new TicketReceipt();
        ticketReceipt.setAirlineAccountCode("001");
        ticketReceipt.setDepartureDate(dateFormat.parse("09/30/2019"));
        ticketReceipt.setDestinationAirport(getAirport("MIA", "Miami International", "Miami", "FL", "USA", "United States"));
        ticketReceipt.setOriginAirport(getAirport("MCO", "Orlando International", "Orlando", "FL", "USA", "United States"));
        ticketReceipt.setPnr("MRYMPT");
        ticketReceipt.setTicketIssueDate(dateFormat.parse("03/14/2019"));

        PassengerDetail passengerDetail = new PassengerDetail();
        passengerDetail.setTicketNumber("2371661425");
        passengerDetail.setFirstName("SIMON");
        passengerDetail.setLastName("TEST");
        passengerDetail.setAdvantageNumber("XYZ1234");
        passengerDetail.setLoyaltyOwnerCode(("AA"));
        passengerDetail.setPassengerTotalAmount("123.45");

        List<FormOfPayment> formOfPaymentList = new ArrayList<>();
        FormOfPayment fop = new FormOfPayment(dateFormat.parse("03/14/2019"), "CCBA", "0006", "225295", "USD2");

        Set<Ancillary> ancillaries = new HashSet<>();
        ancillaries.add(new Ancillary("654200213", "2019-11-07", "090", "MAIN CABIN EXTRA (DFW - BDL)", "72.91", "USD", "78.38", "USD", "5.47"));
        fop.setAncillaries(ancillaries);

        formOfPaymentList.add(fop);
        passengerDetail.setFormOfPayments(formOfPaymentList);

        FareTaxesFees fareTaxesFees = new FareTaxesFees("77674", "USD2", "84930", "7256");
        passengerDetail.setFareTaxesFees(fareTaxesFees);

        ticketReceipt.getPassengerDetails().add(passengerDetail);

        return ticketReceipt;
    }

    public static Airport getAirport(String code, String name, String city, String state, String countryCode, String countryName) {
        Airport airport = new Airport();
        airport.setCode(code);
        airport.setCity(city);
        airport.setCountryCode(countryCode);
        airport.setCountryName(countryName);
        airport.setName(name);
        airport.setStateCode(state);
        return airport;
    }
}
