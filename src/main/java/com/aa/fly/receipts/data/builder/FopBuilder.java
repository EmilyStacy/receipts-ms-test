package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import org.springframework.stereotype.Component;


@Component
public class FopBuilder implements DataBuilderService {

    @Override
	public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {

        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(ticketReceiptRsRow.getFopIssueDt());
        formOfPayment.setFopTypeCode(ticketReceiptRsRow.getFopTypeCd());
        formOfPayment.setFopAmount(ticketReceiptRsRow.getFopAmt());
        formOfPayment.setFopAccountNumberLast4(ticketReceiptRsRow.getFopAcctNbrLast4());
        formOfPayment.setFopCurrencyCode(ticketReceiptRsRow.getFopCurrTypeCd());

        ticketReceipt.getFormOfPayments().add(formOfPayment);

        return ticketReceipt;

    }
	

}

