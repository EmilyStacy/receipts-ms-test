package com.aa.fly.receipts.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.config.AppConfig;
import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.Tax;

@RunWith(SpringJUnit4ClassRunner.class)
public class CostDetailsMapperTest {

    @Mock
    private SqlRowSet resultSet;

    @Mock
    private CreditCardAliasRepository creditCardAliasRepository;

    @Mock
    private TaxDescriptionRepository taxDescriptionRepository;

    @InjectMocks
    private CostDetailsMapper costDetailsMapper;

    @InjectMocks
    private TicketReceiptMapper ticketReceiptMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testMapCostDetails() throws ParseException {
        PassengerDetail passengerDetail = new PassengerDetail();

        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString("FOP_ACCT_NBR_LAST4")).thenReturn("0006");
        Mockito.when(resultSet.getDate("FOP_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        Mockito.when(resultSet.getString("FOP_AMT")).thenReturn("225295");
        Mockito.when(resultSet.getString("FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("FOP_TYPE_CD")).thenReturn("CCBA");

        Mockito.when(resultSet.getString("FNUM_FARE_AMT")).thenReturn("77674");
        Mockito.when(resultSet.getString("FNUM_FARE_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("EQFN_FARE_AMT")).thenReturn("0");
        Mockito.when(resultSet.getString("EQFN_FARE_CURR_TYPE_CD")).thenReturn("");
        Mockito.when(resultSet.getString("FARE_TDAM_AMT")).thenReturn("84930");

        Mockito.when(resultSet.getString("TAX_CD_SEQ_ID")).thenReturn("1");
        Mockito.when(resultSet.getString("TAX_CD")).thenReturn("XA");
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("450");
        Mockito.when(resultSet.getString("TAX_CURR_TYPE_CD")).thenReturn("USD2");

        Mockito.when(resultSet.getString("ANCLRY_DOC_NBR")).thenReturn("654200213");
        Mockito.when(resultSet.getString("ANCLRY_ISSUE_DT")).thenReturn("2019-11-07");
        Mockito.when(resultSet.getString("ANCLRY_PROD_CD")).thenReturn("090");
        Mockito.when(resultSet.getString("ANCLRY_PROD_NM")).thenReturn("MAIN CABIN EXTRA");
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn("DFW");
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn("BDL");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")).thenReturn("72.91");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_AMT")).thenReturn("78.38");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_CD")).thenReturn("USD");

        Mockito.when(resultSet.getString("ANCLRY_FOP_ACCT_NBR_LAST4")).thenReturn("1111");
        Mockito.when(resultSet.getString("ANCLRY_FOP_AMT")).thenReturn("53628");
        Mockito.when(resultSet.getString("ANCLRY_FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("ANCLRY_FOP_TYPE_CD")).thenReturn("CCBA");

        Ancillary ancillary = new Ancillary("654200213", "2019-11-07", "090", "MAIN CABIN EXTRA (DFW - BDL)", "72.91", "USD", "78.38", "USD", "5.47");

        costDetailsMapper.setFopTypeMap(new AppConfig().fopTypeMap());
        Mockito.when(creditCardAliasRepository.getCreditCardAliasMap()).thenReturn(fopTypeMap());

        passengerDetail = costDetailsMapper.mapCostDetails(resultSet, passengerDetail);
        List<FormOfPayment> fops = passengerDetail.getFormOfPayments();

        assertThat(fops.size()).isEqualTo(2);
        assertThat(fops.get(0).getFopAccountNumberLast4()).isEqualTo("0006");
        assertThat(fops.get(0).getFopIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(fops.get(0).getFopAmount()).isEqualTo("2252.95");
        assertThat(fops.get(0).getFopCurrencyCode()).isEqualTo("USD");
        assertThat(fops.get(0).getFopTypeCode()).isEqualTo("CCBA");

        assertThat(fops.get(1).getAncillaries()).contains(ancillary);
    }


    @org.junit.Test
    public void adjustTaxesWithOtherCurrenciesWhenPassengerDetailIsNull() {
        PassengerDetail passengerDetail = null;
        assertThat(costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail)).isNull();
    }

    @org.junit.Test
    public void adjustTaxesWithOtherCurrenciesWhenFareTaxFeesIsNull() {
        PassengerDetail passengerDetail = new PassengerDetail();
        assertThat(costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail)).isEqualTo(passengerDetail);
    }

    @org.junit.Test
    public void testAdjustTaxesWithOtherCurrencies_oneCreditCardAsFop() {
        PassengerDetail passengerDetail = new PassengerDetail();
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setTotalFareAmount("1000.00");
        fareTaxesFees.setBaseFareAmount("700");
        fareTaxesFees.setBaseFareCurrencyCode("USD");

        Tax gbTax = new Tax();
        gbTax.setTaxCode("GB");
        gbTax.setTaxAmount("200");
        gbTax.setTaxCodeSequenceId("1");
        gbTax.setTaxCurrencyCode("USD");

        Tax xfTax = new Tax();
        xfTax.setTaxCode("XF");
        xfTax.setTaxAmount("75");
        xfTax.setTaxCodeSequenceId("2");
        xfTax.setTaxCurrencyCode("CAD");

        Tax xaTax = new Tax();
        xaTax.setTaxCode("XA");
        xaTax.setTaxAmount("50");
        xaTax.setTaxCodeSequenceId("3");
        xaTax.setTaxCurrencyCode("USD");

        fareTaxesFees.getTaxes().add(gbTax);
        fareTaxesFees.getTaxes().add(xfTax);
        fareTaxesFees.getTaxes().add(xaTax);

        passengerDetail.setFareTaxesFees(fareTaxesFees);

        costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail);

        Tax adjustedTax = fareTaxesFees.getTaxes().stream().filter(t -> "XF".equals(t.getTaxCode())).findFirst().orElse(null);
        assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
        assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("USD");
        assertThat(adjustedTax.getTaxAmount()).isEqualTo("50.00");
    }

    @Test
    public void testAdjustTaxesWithOtherCurrencies_XF_deducted()  {
        PassengerDetail passengerDetail = new PassengerDetail();
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setTotalFareAmount("1000.00");
        fareTaxesFees.setBaseFareAmount("700");
        fareTaxesFees.setBaseFareCurrencyCode("CAD");

        Tax gbTax = new Tax();
        gbTax.setTaxCode("GB");
        gbTax.setTaxAmount("151.1");
        gbTax.setTaxCodeSequenceId("1");
        gbTax.setTaxCurrencyCode("CAD");

        Tax xfTax = new Tax();
        xfTax.setTaxCode("XF");
        xfTax.setTaxAmount("75");
        xfTax.setTaxCodeSequenceId("2");
        xfTax.setTaxCurrencyCode("USD");

        Tax xaTax = new Tax();
        xaTax.setTaxCode("XA");
        xaTax.setTaxAmount("50");
        xaTax.setTaxCodeSequenceId("3");
        xaTax.setTaxCurrencyCode("CAD");

        fareTaxesFees.getTaxes().add(gbTax);
        fareTaxesFees.getTaxes().add(xfTax);
        fareTaxesFees.getTaxes().add(xaTax);

        passengerDetail.setFareTaxesFees(fareTaxesFees);

        costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail);

        Tax adjustedTax = fareTaxesFees.getTaxes().stream().filter(t -> "XF".equals(t.getTaxCode())).findFirst().orElse(null);

        assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
        assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("CAD");
        assertThat(adjustedTax.getTaxAmount()).isEqualTo("98.90");

    }

    @Test
    public void testMapFormOfPayment_retrunTrueForCreditCard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapFormOfPayment", String.class);
        method.setAccessible(true);
        boolean returnValue = (boolean) method.invoke(costDetailsMapper, "CC");
        assertThat(returnValue).isTrue();
    }

    @Test
    public void testMapFormOfPayment_retrunTrueForCash() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapFormOfPayment", String.class);
        method.setAccessible(true);
        boolean returnValue = (boolean) method.invoke(costDetailsMapper, "CA");
        assertThat(returnValue).isTrue();
    }

    @Test
    public void testMapFormOfPayment_retrunFalseForExchange() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapFormOfPayment", String.class);
        method.setAccessible(true);
        boolean returnValue = (boolean) method.invoke(costDetailsMapper, "EF");
        assertThat(returnValue).isFalse();
    }

    @Test
    public void testMapTaxDescriptionCAD() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapTax", SqlRowSet.class,String.class);
        method.setAccessible(true);
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("4.20");
        Mockito.when(resultSet.getString("TAX_CURR_TYPE_CD")).thenReturn("CAD");
        Mockito.when(resultSet.getString("TAX_CD")).thenReturn("XF");
        Mockito.when(resultSet.getString("CITY_CD")).thenReturn("DFW");
        Mockito.when(taxDescriptionRepository.getDescription(eq("XF"), any())).thenReturn("SYS GEN PFC");
        Tax returnValue = (Tax) method.invoke(costDetailsMapper, resultSet, "USD");
        assertThat(returnValue.getTaxDescription()).isEqualTo("SYS GEN PFC");
        assertThat(returnValue.getTaxCurrencyCode()).isEqualTo("CAD");

    }

    @Test
    public void testMapTax_descriptionShouldContain3letterAirportCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapTax", SqlRowSet.class, String.class);
        method.setAccessible(true);
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("4.20");
        Mockito.when(resultSet.getString("TAX_CURR_TYPE_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("TAX_CD")).thenReturn("XF");
        Mockito.when(resultSet.getString("CITY_CD")).thenReturn("DFW");
        Mockito.when(taxDescriptionRepository.getDescription(eq("XF"), any())).thenReturn("SYS GEN PFC");
        Tax returnValue = (Tax) method.invoke(costDetailsMapper, resultSet, "USD");
        assertThat(returnValue.getTaxDescription()).endsWith("(DFW)");
    }


    @Test
    public void sumFopAmounts_evenExchange_passengerTotalAmountShouldBeEqualFareTotalAmount() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PassengerDetail passengerDetail = new PassengerDetail();
        passengerDetail.setFormOfPayments(new ArrayList<>());
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setTotalFareAmount("1016.59");
        fareTaxesFees.setTaxFareAmount("97.53");
        fareTaxesFees.setBaseFareAmount("919.06");
        passengerDetail.setFareTaxesFees(fareTaxesFees);
        Method method = costDetailsMapper.getClass().getDeclaredMethod("sumFopAmounts", PassengerDetail.class);
        method.setAccessible(true);
        PassengerDetail returnValue = (PassengerDetail) method.invoke(costDetailsMapper, passengerDetail);
        assertThat(returnValue.getPassengerTotalAmount()).isEqualTo(passengerDetail.getPassengerTotalAmount());
    }


    public Map<String, String> fopTypeMap() {
        Map<String, String> fopTypeMap = new HashMap<>();
        fopTypeMap.put("CCAX", "American Express");
        fopTypeMap.put("CCDC", "Diners Club");
        fopTypeMap.put("CCDS", "Discover");
        fopTypeMap.put("CCBA", "Visa");
        fopTypeMap.put("CCVI", "Visa");
        fopTypeMap.put("CCIK", "Mastercard");
        fopTypeMap.put("CCMC", "Mastercard");
        fopTypeMap.put("CCCA", "Mastercard");
        return fopTypeMap;
    }
}
