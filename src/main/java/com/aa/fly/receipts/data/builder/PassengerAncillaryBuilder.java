package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Component
public class PassengerAncillaryBuilder {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Set<Ancillary> build( TicketReceiptRsRow ticketReceiptRsRow ) {

        String ancillaryDocNumber = ticketReceiptRsRow.getAnclryDocNbr();
        Set<Ancillary> ancillaryList = new HashSet<>();

        if (StringUtils.isNotBlank(ancillaryDocNumber)) {
            Ancillary ancillary = new Ancillary();
            ancillary.setAnclryDocNbr(ancillaryDocNumber);
            ancillary.setAnclryIssueDate(ticketReceiptRsRow.getAnclryIssueDt() != null ? dateFormat.format(ticketReceiptRsRow.getAnclryIssueDt()) : "");
            ancillary.setAnclryProdCode(ticketReceiptRsRow.getAnclryProdCd());

            String segDeptArprtCd = ticketReceiptRsRow.getSegDeptArprtCd();
            String segArvlArprtCd = ticketReceiptRsRow.getSegArvlArprtCd();
            String anclryProdName = ticketReceiptRsRow.getAnclryProdNm();
            ancillary.setAnclryProdName(anclryProdName);

            if (StringUtils.isNotBlank(anclryProdName) && StringUtils.isNotBlank(segDeptArprtCd) && StringUtils.isNotBlank(segArvlArprtCd)) {
                ancillary.setAnclryProdName(anclryProdName + " (" + segDeptArprtCd + " - " + segArvlArprtCd + ")");
            }

            String ancillaryPriceCurrencyAmount = ticketReceiptRsRow.getAnclryPriceLclCurncyAmt();
            ancillary.setAnclryPriceCurrencyAmount(ancillaryPriceCurrencyAmount);
            ancillary.setAnclryPriceCurrencyCode(ticketReceiptRsRow.getAnclryPriceLclCurncyCd());

            String anclrySalesCurrencyAmount = ticketReceiptRsRow.getAnclrySlsCurncyAmt();
            ancillary.setAnclrySalesCurrencyAmount(anclrySalesCurrencyAmount);
            ancillary.setAnclrySalesCurrencyCode(ticketReceiptRsRow.getAnclrySlsCurncyCd());

            BigDecimal anclryTaxCurrencyAmount = new BigDecimal(anclrySalesCurrencyAmount).subtract(new BigDecimal(ancillaryPriceCurrencyAmount)).setScale(2, RoundingMode.CEILING);
            ancillary.setAnclryTaxCurrencyAmount(anclryTaxCurrencyAmount.toString());
            ancillaryList.add(ancillary);
        }
        return ancillaryList;
    }
}
