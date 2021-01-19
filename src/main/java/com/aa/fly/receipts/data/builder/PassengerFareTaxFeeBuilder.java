package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class PassengerFareTaxFeeBuilder implements DataBuilderService {


    @Override
    public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {

        FareTaxesFees fareTaxesFees = new FareTaxesFees();
        String baseFareAmount;
        String baseFareCurrencyCode;


        if ((ticketReceiptRsRow.getEqfnFareAmt().equals("0") && !ticketReceiptRsRow.getFnumFareAmt().equals("0")) || ticketReceiptRsRow.getEqfnFareCurrTypeCd().isEmpty()) {
            baseFareAmount = ticketReceiptRsRow.getFnumFareAmt();
            baseFareCurrencyCode = ticketReceiptRsRow.getFnumFareCurrTypeCd();
        } else {
            baseFareAmount = ticketReceiptRsRow.getEqfnFareAmt();
            baseFareCurrencyCode = ticketReceiptRsRow.getEqfnFareCurrTypeCd();
        }


        AmountAndCurrency baseFareAmountAndCurrency = new AmountAndCurrency(baseFareAmount, baseFareCurrencyCode);
        AmountAndCurrency totalFareAmountAndCurrency = new AmountAndCurrency(ticketReceiptRsRow.getFareTdamAmt(), baseFareCurrencyCode);

        fareTaxesFees.setBaseFareCurrencyCode(baseFareAmountAndCurrency.getCurrencyCode());
        fareTaxesFees.setBaseFareAmount(baseFareAmountAndCurrency.getAmount());
        fareTaxesFees.setTotalFareAmount(totalFareAmountAndCurrency.getAmount());

        BigDecimal totalFareAmountDecimal = new BigDecimal(fareTaxesFees.getTotalFareAmount());
        BigDecimal baseFareAmountDecimal = new BigDecimal(fareTaxesFees.getBaseFareAmount());
        BigDecimal totalTaxAmount = totalFareAmountDecimal.subtract(baseFareAmountDecimal);
        fareTaxesFees.setTaxFareAmount(totalTaxAmount.toString());

        ticketReceipt.getPassengerDetails().get(0).setFareTaxesFees(fareTaxesFees);
        return ticketReceipt;
    }


}
