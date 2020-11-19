package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.data.TaxDescriptionRepository;
import com.aa.fly.receipts.domain.*;
import com.aa.fly.receipts.service.DataBuilderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassengerTaxFeeItemBuilder implements DataBuilderService {

    @Autowired
    private TaxDescriptionRepository taxDescriptionRepository = new TaxDescriptionRepository();

    @Override
    public TicketReceipt build(TicketReceipt ticketReceipt, TicketReceiptRsRow ticketReceiptRsRow) {

        Tax tax = new Tax();
        tax.setTaxCodeSequenceId(ticketReceiptRsRow.getTaxCdSeqId());
        tax.setTaxCode(ticketReceiptRsRow.getTaxCd());

        AmountAndCurrency amountAndCurrency = new AmountAndCurrency(ticketReceiptRsRow.getTaxAmt(), ticketReceiptRsRow.getTaxCurrTypeCd());
        tax.setTaxAmount(amountAndCurrency.getAmount());
        tax.setTaxCurrencyCode(amountAndCurrency.getCurrencyCode());

        String cityCode = StringUtils.isNotBlank(ticketReceiptRsRow.getCityCd()) ? ticketReceiptRsRow.getCityCd().trim() : "";
        tax.setCityCode(cityCode);

        String description = taxDescriptionRepository.getDescription(tax.getTaxCode(), ticketReceiptRsRow.getTicketIssueDt());
        if ("USD".equals(ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode())) {
            cityCode = cityCode.length() == 0 ? cityCode : "(".concat(cityCode).concat(")");
            description = cityCode.length() == 0 ? description : description.concat(" ").concat(cityCode);

        }
        tax.setTaxDescription(description);
        ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTaxes().add(tax);
        return ticketReceipt;
    }
}
