package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.data.CreditCardAliasRepository;
import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class PassengerAncillaryFopBuilder implements DataBuilderService {

    @Autowired
    private CreditCardAliasRepository creditCardAliasRepository;

    private Map<String, String> fopTypeMap;

    @Autowired
    public void setFopTypeMap(Map<String, String> fopTypeMap) {
        this.fopTypeMap = fopTypeMap;
    }

    @Override
    public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {

        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopIssueDate(ticketReceiptRsRow.getAnclryIssueDt());
        formOfPayment.setFopTypeCode(ticketReceiptRsRow.getAnclryFopTypeCd());
        formOfPayment.setFopTypeDescription(getFormOfPaymentDescription(ticketReceiptRsRow.getAnclryFopTypeCd(),ticketReceiptRsRow.getAnclryFopAcctNbrLast4()));
        formOfPayment.setFopAccountNumberLast4(ticketReceiptRsRow.getAnclryFopAcctNbrLast4());
        formOfPayment.setFopAmount(ticketReceiptRsRow.getAnclryFopAmt());
        formOfPayment.setFopCurrencyCode(ticketReceiptRsRow.getAnclryFopCurrTypeCd());

        ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().add(formOfPayment);

        return ticketReceipt;
    }

    private String getFormOfPaymentDescription(String fopTypeCode, String last4) {
        String description = "";

        if (StringUtils.isNotBlank(fopTypeCode)) {
            if (fopTypeCode.startsWith("CC") && StringUtils.isNotBlank(last4)) {
                description = creditCardAliasRepository.getCreditCardAliasMap().get(fopTypeCode);
                description = description + " ending in " + last4;
            } else {
                description = fopTypeMap.get(fopTypeCode);
            }
        }

        return StringUtils.isBlank(description) ? "" : description;
    }

}

