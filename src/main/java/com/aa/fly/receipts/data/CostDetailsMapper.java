package com.aa.fly.receipts.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.FormOfPaymentKey;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.Tax;

@Component
public class CostDetailsMapper {

    private Map<String, String> fopTypeMap;

    @Autowired
    private CreditCardAliasRepository creditCardAliasRepository;

    @Autowired
    private TaxDescriptionRepository taxDescriptionRepository;

    @Autowired
    public void setFopTypeMap(Map<String, String> fopTypeMap) {
        this.fopTypeMap = fopTypeMap;
    }

    private static final String FOP_TYPE_CD = "FOP_TYPE_CD";
    private static final String ANCLRY_ISSUE_DT = "ANCLRY_ISSUE_DT";
    private static final String ANCLRY_PRICE_LCL_CURNCY_AMT = "ANCLRY_PRICE_LCL_CURNCY_AMT";

    public PassengerDetail mapCostDetails(SqlRowSet rs, PassengerDetail passengerDetail) {
        List<FormOfPayment> formOfPayments = new ArrayList<>();
        Set<String> anclryDocNums = new HashSet<>();
        FareTaxesFees fareTaxesFees = null;
        int rowCount = 0;
        Set<FormOfPaymentKey> fopKeys = new HashSet<>();
        while (rs.next()) {
            String fopSequenceId = StringUtils.isNotBlank(rs.getString("FOP_SEQ_ID")) ? rs.getString("FOP_SEQ_ID").trim() : "";
            String fopTypeCode = StringUtils.isNotBlank(rs.getString(FOP_TYPE_CD)) ? rs.getString(FOP_TYPE_CD).trim() : null;
            FormOfPaymentKey formOfPaymentKey = new FormOfPaymentKey(fopSequenceId, fopTypeCode);

            if (rowCount == 0) {
                mapFormOfPayment(rs, formOfPayments);
                fareTaxesFees = mapFareTaxAndFees(rs);
                passengerDetail.setFareTaxesFees(fareTaxesFees);
            } else {

                if(!fopKeys.contains(formOfPaymentKey) && mapFormOfPayment(fopTypeCode)) {
                    mapFormOfPayment(rs, formOfPayments);
                    formOfPayments = adjustFormOfPaymentsIfExchanged(formOfPayments);
                }
                fareTaxesFees.getTaxes().add(mapTax(rs));
            }

            passengerDetail.setFormOfPayments(formOfPayments);
            fopKeys.add(formOfPaymentKey);
            rowCount++;

            mapAnclry(rs, formOfPayments, anclryDocNums);
        }

        return adjustTaxesWithOtherCurrencies(sumFopAmounts(passengerDetail));
    }

    private PassengerDetail sumFopAmounts(PassengerDetail passengerDetail) {
        if (passengerDetail == null)
            return passengerDetail;

        BigDecimal passengerTotalAmount = new BigDecimal("0");

        for (int i = 0; i < passengerDetail.getFormOfPayments().size(); i++) {
            passengerTotalAmount = passengerTotalAmount.add(new BigDecimal(passengerDetail.getFormOfPayments().get(i).getFopAmount())).setScale(2, RoundingMode.CEILING);
        }

        //in case of even exchange, return totalFareAmount as passengerTotalAmount
        if(passengerTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
            passengerTotalAmount = BigDecimal.valueOf(Double.valueOf(passengerDetail.getFareTaxesFees().getTotalFareAmount())).setScale(2, RoundingMode.CEILING);
        }
        passengerDetail.setPassengerTotalAmount(passengerTotalAmount.toString());

        return passengerDetail;
    }

    private void mapFormOfPayment(SqlRowSet rs, List<FormOfPayment> formOfPayments) {
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopAccountNumberLast4(StringUtils.isNotBlank(rs.getString("FOP_ACCT_NBR_LAST4")) ? rs.getString("FOP_ACCT_NBR_LAST4").trim() : null);
        formOfPayment.setFopIssueDate(rs.getDate("FOP_ISSUE_DT"));

        String fopAmount = StringUtils.isNotBlank(rs.getString("FOP_AMT")) ? rs.getString("FOP_AMT").trim() : null;
        String fopCurrencyCode = StringUtils.isNotBlank(rs.getString("FOP_CURR_TYPE_CD")) ? rs.getString("FOP_CURR_TYPE_CD").trim() : "";
        AmountAndCurrency fopAmountAndCurrency = new AmountAndCurrency(fopAmount, fopCurrencyCode);

        formOfPayment.setFopAmount(fopAmountAndCurrency.getAmount());
        formOfPayment.setFopCurrencyCode(fopAmountAndCurrency.getCurrencyCode());

        formOfPayment.setFopTypeCode(StringUtils.isNotBlank(rs.getString(FOP_TYPE_CD)) ? rs.getString(FOP_TYPE_CD).trim() : null);
        formOfPayment.setFopTypeDescription(getFormOfPaymentDescription(formOfPayment.getFopTypeCode(), formOfPayment.getFopAccountNumberLast4()));

        formOfPayments.add(formOfPayment);
    }

    private boolean mapFormOfPayment(String fopTypeCode) {
        if(fopTypeCode == null) return false;
        return fopTypeCode.startsWith("CC") || fopTypeCode.startsWith("CA");
    }


    private List<FormOfPayment> adjustFormOfPaymentsIfExchanged(List<FormOfPayment> formOfPayments) {
        boolean isExchange = formOfPayments.stream().anyMatch(f -> "EF".equals(f.getFopTypeCode()) || "EX".equals(f.getFopTypeCode()));
        if(isExchange) {
            formOfPayments = formOfPayments.stream().filter(f -> BigDecimal.valueOf(Double.valueOf(f.getFopAmount())).compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            formOfPayments.stream().forEach(f -> f.setFopTypeDescription("Exchange - " + f.getFopTypeDescription()));
        }

        return formOfPayments;
    }

    private FormOfPayment mapAnclryFormOfPayment(SqlRowSet rs) {
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopAccountNumberLast4(StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_ACCT_NBR_LAST4")) ? rs.getString("ANCLRY_FOP_ACCT_NBR_LAST4").trim() : null);
        formOfPayment.setFopIssueDate(rs.getDate(ANCLRY_ISSUE_DT));

        String fopAmount = StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_AMT")) ? rs.getString("ANCLRY_FOP_AMT").trim() : null;
        String fopCurrencyCode = StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_CURR_TYPE_CD")) ? rs.getString("ANCLRY_FOP_CURR_TYPE_CD").trim() : "";
        AmountAndCurrency fopAmountAndCurrency = new AmountAndCurrency(fopAmount, fopCurrencyCode);

        formOfPayment.setFopAmount(fopAmountAndCurrency.getAmount());
        formOfPayment.setFopCurrencyCode(fopAmountAndCurrency.getCurrencyCode());

        formOfPayment.setFopTypeCode(StringUtils.isNotBlank(rs.getString("ANCLRY_FOP_TYPE_CD")) ? rs.getString("ANCLRY_FOP_TYPE_CD").trim() : null);
        formOfPayment.setFopTypeDescription(getFormOfPaymentDescription(formOfPayment.getFopTypeCode(), formOfPayment.getFopAccountNumberLast4()));

        return formOfPayment;
    }

    private void mapAnclry(SqlRowSet rs, List<FormOfPayment> formOfPayments, Set<String> anclryDocNums) {
        FormOfPayment formOfPayment = null;
        Ancillary ancillary = null;

        String anclryDocNbr = rs.getString("ANCLRY_DOC_NBR");

        if (StringUtils.isNotBlank(anclryDocNbr) && !anclryDocNums.contains(anclryDocNbr)) {
            formOfPayment = mapAnclryFormOfPayment(rs);

            ancillary = new Ancillary();
            ancillary.setAnclryDocNbr(anclryDocNbr);
            ancillary.setAnclryIssueDate(StringUtils.isNotBlank(rs.getString(ANCLRY_ISSUE_DT)) ? rs.getString(ANCLRY_ISSUE_DT).trim() : null);
            ancillary.setAnclryProdCode(StringUtils.isNotBlank(rs.getString("ANCLRY_PROD_CD")) ? rs.getString("ANCLRY_PROD_CD").trim() : null);
            String anclryProdName = StringUtils.isNotBlank(rs.getString("ANCLRY_PROD_NM")) ? rs.getString("ANCLRY_PROD_NM").trim() : "???";
            String segDeptArprtCd = StringUtils.isNotBlank(rs.getString("SEG_DEPT_ARPRT_CD")) ? rs.getString("SEG_DEPT_ARPRT_CD").trim() : null;
            String segArvlArprtCd = StringUtils.isNotBlank(rs.getString("SEG_ARVL_ARPRT_CD")) ? rs.getString("SEG_ARVL_ARPRT_CD").trim() : null;

            if (anclryProdName != null) {
                ancillary.setAnclryProdName(anclryProdName + " (" + segDeptArprtCd + " - " + segArvlArprtCd + ")");
            }

            String anclryPriceCurrencyAmount = StringUtils.isNotBlank(rs.getString(ANCLRY_PRICE_LCL_CURNCY_AMT)) ? rs.getString(ANCLRY_PRICE_LCL_CURNCY_AMT).trim() : null;
            ancillary.setAnclryPriceCurrencyAmount(StringUtils.isNotBlank(rs.getString(ANCLRY_PRICE_LCL_CURNCY_AMT)) ? rs.getString(ANCLRY_PRICE_LCL_CURNCY_AMT).trim() : null);

            ancillary.setAnclryPriceCurrencyCode(StringUtils.isNotBlank(rs.getString("ANCLRY_PRICE_LCL_CURNCY_CD")) ? rs.getString("ANCLRY_PRICE_LCL_CURNCY_CD").trim() : null);

            String anclrySalesCurrencyAmount = StringUtils.isNotBlank(rs.getString("ANCLRY_SLS_CURNCY_AMT")) ? rs.getString("ANCLRY_SLS_CURNCY_AMT").trim() : null;
            ancillary.setAnclrySalesCurrencyAmount(anclrySalesCurrencyAmount);

            ancillary.setAnclrySalesCurrencyCode(StringUtils.isNotBlank(rs.getString("ANCLRY_SLS_CURNCY_CD")) ? rs.getString("ANCLRY_SLS_CURNCY_CD").trim() : null);

            BigDecimal anclryTaxCurrencyAmount = new BigDecimal(anclrySalesCurrencyAmount).subtract(new BigDecimal(anclryPriceCurrencyAmount)).setScale(2, RoundingMode.CEILING);

            ancillary.setAnclryTaxCurrencyAmount(anclryTaxCurrencyAmount.toString());

            anclryDocNums.add(anclryDocNbr);

            formOfPayment.getAncillaries().add(ancillary);
            formOfPayments.add(formOfPayment);
        }
    }

    private FareTaxesFees mapFareTaxAndFees(SqlRowSet rs) {

        FareTaxesFees fareTaxesFees = new FareTaxesFees();

        String baseFareAmount;
        String baseFareCurrencyCode;

        String totalFareAmount = rs.getString("FARE_TDAM_AMT");

        int eqfnFareAmt = Integer.parseInt(rs.getString("EQFN_FARE_AMT"));
        if (eqfnFareAmt == 0) {
            baseFareAmount = rs.getString("FNUM_FARE_AMT");
            baseFareCurrencyCode = rs.getString("FNUM_FARE_CURR_TYPE_CD");
        } else {
            baseFareAmount = rs.getString("EQFN_FARE_AMT");
            baseFareCurrencyCode = rs.getString("EQFN_FARE_CURR_TYPE_CD");
        }

        AmountAndCurrency baseFareAmountAndCurrency = new AmountAndCurrency(baseFareAmount, baseFareCurrencyCode);
        AmountAndCurrency totalFareAmountAndCurrency = new AmountAndCurrency(totalFareAmount, baseFareCurrencyCode);

        fareTaxesFees.setBaseFareCurrencyCode(baseFareAmountAndCurrency.getCurrencyCode());
        fareTaxesFees.setBaseFareAmount(baseFareAmountAndCurrency.getAmount());
        fareTaxesFees.setTotalFareAmount(totalFareAmountAndCurrency.getAmount());
        fareTaxesFees.getTaxes().add(mapTax(rs));
        return fareTaxesFees;
    }

    private Tax mapTax(SqlRowSet rs) {
        Tax tax = new Tax();
        tax.setTaxCodeSequenceId(rs.getString("TAX_CD_SEQ_ID"));
        tax.setTaxCode(rs.getString("TAX_CD").trim());
        String cityCode = StringUtils.isNotBlank(rs.getString("CITY_CD")) ? rs.getString("CITY_CD").trim() : ""  ;
        cityCode = cityCode.length() == 0 ? cityCode : "(".concat(cityCode).concat(")");

        String description = taxDescriptionRepository.getDescription(tax.getTaxCode(), rs.getDate("TICKET_ISSUE_DT"));
        description = cityCode.length() == 0 ? description : description.concat(" ").concat(cityCode);

        tax.setTaxDescription(description);
        AmountAndCurrency amountAndCurrency = new AmountAndCurrency(rs.getString("TAX_AMT"), rs.getString("TAX_CURR_TYPE_CD"));
        tax.setTaxAmount(amountAndCurrency.getAmount());
        tax.setTaxCurrencyCode(amountAndCurrency.getCurrencyCode());
        return tax;
    }

    public PassengerDetail adjustTaxesWithOtherCurrencies(PassengerDetail passengerDetail) {
        if (passengerDetail == null || passengerDetail.getFareTaxesFees() == null)
            return passengerDetail;

        FareTaxesFees fareTaxesFees = passengerDetail.getFareTaxesFees();
        String baseFareCurrencyCode = fareTaxesFees.getBaseFareCurrencyCode();
        BigDecimal totalFareAmount = new BigDecimal(fareTaxesFees.getTotalFareAmount());
        BigDecimal baseFareAmount = new BigDecimal(fareTaxesFees.getBaseFareAmount());
        BigDecimal totalTaxAmount = totalFareAmount.subtract(baseFareAmount);
        fareTaxesFees.setTaxFareAmount(totalTaxAmount.toString());

        Set<Tax> taxes = passengerDetail.getFareTaxesFees().getTaxes();

        long count = taxes.stream().filter(t -> !baseFareCurrencyCode.equals(t.getTaxCurrencyCode())).count();

        if (count > 0) {
            double baseFareCurencyTaxAmoutDouble = taxes.stream().filter(t -> baseFareCurrencyCode.equals(t.getTaxCurrencyCode())).mapToDouble(t -> Double.valueOf(t.getTaxAmount())).sum();
            BigDecimal baseFareCurrencyTax = BigDecimal.valueOf(baseFareCurencyTaxAmoutDouble);
            String taxAmount = (totalTaxAmount.subtract(baseFareCurrencyTax)).divide(new BigDecimal(count)).setScale(2, RoundingMode.CEILING).toString();
            taxes.stream().filter(t -> !baseFareCurrencyCode.equals(t.getTaxCurrencyCode())).forEach(t -> {
                t.setTaxAmount(taxAmount);
                t.setTaxCurrencyCode(baseFareCurrencyCode);
            });
        }

        return passengerDetail;
    }

    private String getFormOfPaymentDescription(String fopTypeCode, String last4) {
        String description = fopTypeMap.get(fopTypeCode);

        if(description == null) {
            description = creditCardAliasRepository.getCreditCardAliasMap().get(fopTypeCode);
        }

        if(description == null) {
            description = "Card";
        }

        if(fopTypeCode.startsWith("CA")) {
            description = "Cash / Check";
        } else if(fopTypeCode.startsWith("CC") && last4 != null) {
            description = description + " ending in " + last4;
        }

        return description;
    }
}