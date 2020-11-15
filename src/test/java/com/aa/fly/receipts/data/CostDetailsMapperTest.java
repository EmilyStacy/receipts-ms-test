package com.aa.fly.receipts.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.config.AppConfig;
import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.Tax;
import com.aa.fly.receipts.exception.BulkTicketException;
import com.aa.fly.receipts.util.Constants;

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
        
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setBaseFareAmount(Constants.BASE_FARE_AMOUNT);
        fareTaxesFees.setBaseFareCurrencyCode(Constants.BASE_FARE_CURRENCY_CODE);
        fareTaxesFees.setTotalFareAmount(Constants.TOTAL_FARE_AMOUNT);
        fareTaxesFees.setTaxFareAmount(Constants.TAX_FARE_AMOUNT);
        
        passengerDetail.setFareTaxesFees(fareTaxesFees);

        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString("FLIGHT_NBR")).thenReturn("1112");
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
        Mockito.when(resultSet.getString("rcptfare.TCN_BULK_IND")).thenReturn("    ");

        Mockito.when(resultSet.getString("TAX_CD_SEQ_ID")).thenReturn("1").thenReturn("2").thenReturn("3");
        Mockito.when(resultSet.getString("TAX_CD")).thenReturn("ZP").thenReturn("ZP").thenReturn("ZP");
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("4.30").thenReturn("4.30").thenReturn("8.60");
        Mockito.when(resultSet.getString("TAX_CURR_TYPE_CD")).thenReturn("USD2").thenReturn("USD2").thenReturn("USD2");

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

        assertThat(passengerDetail.isShowPassengerTotal()).isEqualTo(true);
        assertThat(fops.size()).isEqualTo(2);
        assertThat(fops.get(0).getFopAccountNumberLast4()).isEqualTo("0006");
        assertThat(fops.get(0).getFopIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(fops.get(0).getFopAmount()).isEqualTo("2252.95");
        assertThat(fops.get(0).getFopCurrencyCode()).isEqualTo("USD");
        assertThat(fops.get(0).getFopTypeCode()).isEqualTo("CCBA");
        assertThat(passengerDetail.getFareTaxesFees().getTaxFareAmount()).isEqualTo(Constants.TAX_FARE_AMOUNT);
        assertThat(passengerDetail.getFareTaxesFees().getTaxes().size()).isEqualTo(1);

        assertThat(fops.get(1).getAncillaries()).contains(ancillary);
    }

    @Test(expected = BulkTicketException.class) //
    public void findCostDetailsByTicketNumber_ShouldThrowExceptionWhenBulkTicket() throws ParseException {
        PassengerDetail passengerDetail = new PassengerDetail();
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString("FLIGHT_NBR")).thenReturn("1112");
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
        Mockito.when(resultSet.getString("TCN_BULK_IND")).thenReturn(" BT ").thenReturn(" IT ").thenReturn("BT").thenReturn("IT").thenReturn("BULK");
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

        costDetailsMapper.mapCostDetails(resultSet, passengerDetail);
    }

    @Test
    public void testMapAnclry_VerifyProdNameWhenAirCodeIsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapAnclry", SqlRowSet.class, List.class, Set.class);
        method.setAccessible(true);
        Set<String> anclryDocNums = new HashSet<String>();
        Mockito.when(resultSet.getString("ANCLRY_DOC_NBR")).thenReturn("0012111822505");
        Mockito.when(resultSet.getString("ANCLRY_PROD_NM")).thenReturn("MSR-OTHER NON TAXABLE");
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("ANCLRY_ISSUE_DT")).thenReturn("1/26/2020");
        Mockito.when(resultSet.getString("ANCLRY_FOP_AMT")).thenReturn("112685");
        Mockito.when(resultSet.getString("ANCLRY_FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("ANCLRY_FOP_TYPE_CD")).thenReturn("CCAX");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_AMT")).thenReturn("35");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")).thenReturn("35");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_FOP_ACCT_NBR_LAST4")).thenReturn("3003");

        ArrayList<FormOfPayment> fopList = new ArrayList<>();
        costDetailsMapper.setFopTypeMap(new AppConfig().fopTypeMap());
        method.invoke(costDetailsMapper, resultSet, fopList, anclryDocNums);
        FormOfPayment returnFop = fopList.get(0);
        Ancillary ancillary = returnFop.getAncillaries().stream().filter(a -> a.getAnclryProdName() != null).findAny().orElse(null);
        assertEquals("MSR-OTHER NON TAXABLE", ancillary.getAnclryProdName());
    }

    @Test
    public void testMapAnclry_VerifyProdNameWhenAirCodeIsNOTNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapAnclry", SqlRowSet.class, List.class, Set.class);
        method.setAccessible(true);
        Set<String> anclryDocNums = new HashSet<String>();
        Mockito.when(resultSet.getString("ANCLRY_DOC_NBR")).thenReturn("0012111822505");
        Mockito.when(resultSet.getString("ANCLRY_PROD_NM")).thenReturn("MSR-OTHER NON TAXABLE");
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn("DFW");
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn("PHX");
        Mockito.when(resultSet.getString("ANCLRY_ISSUE_DT")).thenReturn("1/26/2020");
        Mockito.when(resultSet.getString("ANCLRY_FOP_AMT")).thenReturn("112685");
        Mockito.when(resultSet.getString("ANCLRY_FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("ANCLRY_FOP_TYPE_CD")).thenReturn("CCAX");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_AMT")).thenReturn("35");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")).thenReturn("35");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_FOP_ACCT_NBR_LAST4")).thenReturn("3003");
        ArrayList<FormOfPayment> fopList = new ArrayList<>();
        costDetailsMapper.setFopTypeMap(new AppConfig().fopTypeMap());
        method.invoke(costDetailsMapper, resultSet, fopList, anclryDocNums);
        FormOfPayment returnFop = fopList.get(0);
        Ancillary ancillary = returnFop.getAncillaries().stream().filter(a -> a.getAnclryProdName() != null).findAny().orElse(null);
        assertEquals("MSR-OTHER NON TAXABLE (DFW - PHX)", ancillary.getAnclryProdName());

    }

    @Test
    public void testMapAnclry_VerifyProdNameWhenONEAirCodeIsNOTNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapAnclry", SqlRowSet.class, List.class, Set.class);
        method.setAccessible(true);
        Set<String> anclryDocNums = new HashSet<String>();
        Mockito.when(resultSet.getString("ANCLRY_DOC_NBR")).thenReturn("0012111822505");
        Mockito.when(resultSet.getString("ANCLRY_PROD_NM")).thenReturn("MSR-OTHER NON TAXABLE");
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn("DFW");
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("ANCLRY_ISSUE_DT")).thenReturn("1/26/2020");
        Mockito.when(resultSet.getString("ANCLRY_FOP_AMT")).thenReturn("112685");
        Mockito.when(resultSet.getString("ANCLRY_FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("ANCLRY_FOP_TYPE_CD")).thenReturn("CCAX");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_AMT")).thenReturn("35");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_AMT")).thenReturn("35");
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("ANCLRY_FOP_ACCT_NBR_LAST4")).thenReturn("3003");
        ArrayList<FormOfPayment> fopList = new ArrayList<>();
        costDetailsMapper.setFopTypeMap(new AppConfig().fopTypeMap());
        method.invoke(costDetailsMapper, resultSet, fopList, anclryDocNums);
        FormOfPayment returnFop = fopList.get(0);
        Ancillary ancillary = returnFop.getAncillaries().stream().filter(a -> a.getAnclryProdName() != null).findAny().orElse(null);
        assertEquals("MSR-OTHER NON TAXABLE", ancillary.getAnclryProdName());

    }

    @Test
    public void testMapCostDetailsForDifferentCurrencyCode() throws ParseException {
        PassengerDetail passengerDetail = new PassengerDetail();
        
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setBaseFareAmount(Constants.BASE_FARE_AMOUNT);
        fareTaxesFees.setBaseFareCurrencyCode(Constants.BASE_FARE_CURRENCY_CODE);
        fareTaxesFees.setTotalFareAmount(Constants.TOTAL_FARE_AMOUNT);
        fareTaxesFees.setTaxFareAmount(Constants.TAX_FARE_AMOUNT);
        
        passengerDetail.setFareTaxesFees(fareTaxesFees);

        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString("FLIGHT_NBR")).thenReturn("1112");
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
        Mockito.when(resultSet.getString("ANCLRY_PRICE_LCL_CURNCY_CD")).thenReturn("CAD");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_AMT")).thenReturn("78.38");
        Mockito.when(resultSet.getString("ANCLRY_SLS_CURNCY_CD")).thenReturn("CAD");

        Mockito.when(resultSet.getString("ANCLRY_FOP_ACCT_NBR_LAST4")).thenReturn("1111");
        Mockito.when(resultSet.getString("ANCLRY_FOP_AMT")).thenReturn("53628");
        Mockito.when(resultSet.getString("ANCLRY_FOP_CURR_TYPE_CD")).thenReturn("USD2");
        Mockito.when(resultSet.getString("ANCLRY_FOP_TYPE_CD")).thenReturn("CCBA");

        Ancillary ancillary = new Ancillary("654200213", "2019-11-07", "090", "MAIN CABIN EXTRA (DFW - BDL)", "72.91", "CAD", "78.38", "CAD", "5.47");

        costDetailsMapper.setFopTypeMap(new AppConfig().fopTypeMap());
        Mockito.when(creditCardAliasRepository.getCreditCardAliasMap()).thenReturn(fopTypeMap());

        passengerDetail = costDetailsMapper.mapCostDetails(resultSet, passengerDetail);
        List<FormOfPayment> fops = passengerDetail.getFormOfPayments();

        assertThat(passengerDetail.isShowPassengerTotal()).isEqualTo(false);
        assertThat(fops.size()).isEqualTo(2);
        assertThat(fops.get(0).getFopAccountNumberLast4()).isEqualTo("0006");
        assertThat(fops.get(0).getFopIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(fops.get(0).getFopAmount()).isEqualTo("2252.95");
        assertThat(fops.get(0).getFopCurrencyCode()).isEqualTo("USD");
        assertThat(fops.get(0).getFopTypeCode()).isEqualTo("CCBA");

        assertThat(fops.get(1).getAncillaries()).contains(ancillary);
    }

    @Test
    public void adjustTaxesWithOtherCurrenciesWhenPassengerDetailIsNull() {
        PassengerDetail passengerDetail = null;
        assertThat(costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail)).isNull();
    }

    @Test
    public void adjustTaxesWithOtherCurrenciesWhenFareTaxFeesIsNull() {
        PassengerDetail passengerDetail = new PassengerDetail();
        assertThat(costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail)).isEqualTo(passengerDetail);
    }

    @Test
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
    public void testAdjustTaxesWithOtherCurrencies_CAD_XF_merged() {
        PassengerDetail passengerDetail = new PassengerDetail();
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setTotalFareAmount("1039.60");
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

        Tax xfTax2 = new Tax();
        xfTax2.setTaxCode("XF");
        xfTax2.setTaxAmount("30");
        xfTax2.setTaxCodeSequenceId("3");
        xfTax2.setTaxCurrencyCode("USD");

        Tax xaTax = new Tax();
        xaTax.setTaxCode("XA");
        xaTax.setTaxAmount("50");
        xaTax.setTaxCodeSequenceId("4");
        xaTax.setTaxCurrencyCode("CAD");

        fareTaxesFees.getTaxes().add(gbTax);
        fareTaxesFees.getTaxes().add(xfTax);
        fareTaxesFees.getTaxes().add(xfTax2);
        fareTaxesFees.getTaxes().add(xaTax);

        passengerDetail.setFareTaxesFees(fareTaxesFees);

        costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail);

        int xfTaxItems = (int) fareTaxesFees.getTaxes().stream().filter(t -> "XF".equals(t.getTaxCode())).count();
        Tax adjustedTax = fareTaxesFees.getTaxes().stream().filter(t -> "XF".equals(t.getTaxCode())).findAny().orElseThrow(null);
        assertEquals(1, xfTaxItems);
        assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
        assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("CAD");
        assertThat(adjustedTax.getTaxAmount()).isEqualTo("138.50");
    }

    @Test
    public void testAdjustTaxesWithOtherCurrencies_USD_XF_NotMerged() {
        PassengerDetail passengerDetail = new PassengerDetail();
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.setTotalFareAmount("1000.00");
        fareTaxesFees.setBaseFareAmount("700");
        fareTaxesFees.setBaseFareCurrencyCode("USD");

        Tax gbTax = new Tax();
        gbTax.setTaxCode("GB");
        gbTax.setTaxAmount("100");
        gbTax.setTaxCodeSequenceId("1");
        gbTax.setTaxCurrencyCode("USD");

        Tax xfTax = new Tax();
        xfTax.setTaxCode("XF");
        xfTax.setTaxAmount("100");
        xfTax.setTaxCodeSequenceId("2");
        xfTax.setTaxCurrencyCode("USD");

        Tax xfTax2 = new Tax();
        xfTax2.setTaxCode("XF");
        xfTax2.setTaxAmount("100");
        xfTax2.setTaxCodeSequenceId("3");
        xfTax2.setTaxCurrencyCode("USD");

        Tax xaTax = new Tax();
        xaTax.setTaxCode("XA");
        xaTax.setTaxAmount("50");
        xaTax.setTaxCodeSequenceId("4");
        xaTax.setTaxCurrencyCode("USD");

        fareTaxesFees.getTaxes().add(gbTax);
        fareTaxesFees.getTaxes().add(xfTax);
        fareTaxesFees.getTaxes().add(xfTax2);
        fareTaxesFees.getTaxes().add(xaTax);

        passengerDetail.setFareTaxesFees(fareTaxesFees);

        costDetailsMapper.adjustTaxesWithOtherCurrencies(passengerDetail);

        int xfTaxItems = (int) fareTaxesFees.getTaxes().stream().filter(t -> "XF".equals(t.getTaxCode())).count();
        Tax adjustedTax = fareTaxesFees.getTaxes().stream().filter(t -> "XF".equals(t.getTaxCode())).findAny().orElseThrow(null);
        assertEquals(2, xfTaxItems);
        assertThat(adjustedTax.getTaxCode()).isEqualTo("XF");
        assertThat(adjustedTax.getTaxCurrencyCode()).isEqualTo("USD");
        assertThat(adjustedTax.getTaxAmount()).isEqualTo("100");
    }

    @Test
    public void testMapFormOfPayment_returnTrueForCreditCard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapFormOfPayment", String.class);
        method.setAccessible(true);
        boolean returnValue = (boolean) method.invoke(costDetailsMapper, "CC");
        assertThat(returnValue).isTrue();
    }

    @Test
    public void testMapFormOfPayment_returnTrueForCash() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapFormOfPayment", String.class);
        method.setAccessible(true);
        boolean returnValue = (boolean) method.invoke(costDetailsMapper, "CA");
        assertThat(returnValue).isTrue();
    }

    @Test
    public void testMapFormOfPayment_returnFalseForExchange() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapFormOfPayment", String.class);
        method.setAccessible(true);
        boolean returnValue = (boolean) method.invoke(costDetailsMapper, "EF");
        assertThat(returnValue).isFalse();
    }

    @Test
    public void adjustFormOfPayment_isEchange_setTypeDescription() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("adjustFormOfPaymentsIfExchanged", List.class);
        method.setAccessible(true);

        FormOfPayment fop1 = new FormOfPayment();
        fop1.setFopTypeCode("EF");
        fop1.setFopAmount("50.00");
        fop1.setFopTypeDescription("VISA");

        List<FormOfPayment> fops = new ArrayList<>();

        fops.add(fop1);

        List<FormOfPayment> returnList = (List<FormOfPayment>) method.invoke(costDetailsMapper, fops);

        assertTrue(returnList.get(0).getFopTypeDescription().startsWith("Exchange"));

    }

    @Test
    public void testMapTaxDescriptionCAD() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = costDetailsMapper.getClass().getDeclaredMethod("mapTax", SqlRowSet.class, String.class);
        method.setAccessible(true);
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("4.20");
        Mockito.when(resultSet.getString("TAX_CURR_TYPE_CD")).thenReturn("CAD");
        Mockito.when(resultSet.getString("TAX_CD")).thenReturn("XF");
        Mockito.when(resultSet.getString("CITY_CD")).thenReturn("DFW");
        Mockito.when(taxDescriptionRepository.getDescription(eq("XF"), any())).thenReturn("SYS GEN PFC");
        Tax returnValue = (Tax) method.invoke(costDetailsMapper, resultSet, "CAD");
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

    @Test
    public void handleZPTaxes_WithSubtotalItem() {
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        Tax subTotalZPLineItem = new Tax("5", "ZP", "U.S. SEGMENT TAX", "", "16.80", "USD");
        fareTaxesFees.getTaxes().add(new Tax("1", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(new Tax("2", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(new Tax("3", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(new Tax("4", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(subTotalZPLineItem);
        costDetailsMapper.handleZPTaxes(fareTaxesFees);
        assertThat(fareTaxesFees.getTaxes().size()).isEqualTo(1);
        assertThat(fareTaxesFees.getTaxes().contains(subTotalZPLineItem)).isTrue();
    }

    @Test
    public void handleZPTaxes_WithoutSubtotalItem() {
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.getTaxes().add(new Tax("1", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(new Tax("2", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(new Tax("3", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(new Tax("4", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        costDetailsMapper.handleZPTaxes(fareTaxesFees);
        assertThat(fareTaxesFees.getTaxes().size()).isEqualTo(4);
    }

    @Test
    public void handleZPTaxes_WithOneZPItem() {
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.getTaxes().add(new Tax("1", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        costDetailsMapper.handleZPTaxes(fareTaxesFees);
        assertThat(fareTaxesFees.getTaxes().size()).isEqualTo(1);
    }

    @Test
    public void handleZPTaxes_WithTwoZPItems() {
        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        fareTaxesFees.getTaxes().add(new Tax("1", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        fareTaxesFees.getTaxes().add(new Tax("2", "ZP", "U.S. SEGMENT TAX", "", "4.20", "USD"));
        costDetailsMapper.handleZPTaxes(fareTaxesFees);
        assertThat(fareTaxesFees.getTaxes().size()).isEqualTo(1);
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
