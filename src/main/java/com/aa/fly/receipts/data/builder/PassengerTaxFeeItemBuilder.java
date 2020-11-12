package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.*;
import com.aa.fly.receipts.service.DataBuilderService;
import org.springframework.stereotype.Component;


@Component
public class PassengerTaxFeeItemBuilder implements DataBuilderService {

    @Override
    public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {

        Tax tax = new Tax();

        tax.setTaxCodeSequenceId(ticketReceiptRsRow.getTaxCdSeqId());
        tax.setTaxCode(ticketReceiptRsRow.getTaxCd());
//        tax.setTaxDescription(ticketReceiptRsRow.);
        tax.setCityCode(ticketReceiptRsRow.getCityCd());
        tax.setTaxAmount(ticketReceiptRsRow.getTaxAmt());
        tax.setTaxCurrencyCode(ticketReceiptRsRow.getTaxCurrTypeCd());


        ticketReceipt.getPassengerDetails().get(ticketReceipt.getPassengerDetails().size()-1).getFareTaxesFees().getTaxes().add(tax);
        return ticketReceipt;
    }
}
