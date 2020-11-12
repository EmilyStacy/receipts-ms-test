package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import org.springframework.stereotype.Component;



@Component
public class PassengerFareTaxFeeBuilder implements DataBuilderService {

    @Override
    public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {

        FareTaxesFees fareTaxesFees = new FareTaxesFees();

        fareTaxesFees.setBaseFareAmount(ticketReceiptRsRow.getFnumFareAmt());
        fareTaxesFees.setBaseFareCurrencyCode(ticketReceiptRsRow.getFnumFareCurrTypeCd());
        fareTaxesFees.setTotalFareAmount(ticketReceiptRsRow.getFareTdamAmt());
        fareTaxesFees.setTaxFareAmount(ticketReceiptRsRow.getTaxAmt());

        ticketReceipt.getPassengerDetails().get(0).setFareTaxesFees(fareTaxesFees);
        return ticketReceipt;
    }
}
