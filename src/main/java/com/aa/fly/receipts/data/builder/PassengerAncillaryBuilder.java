package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.Ancillary;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Component
public class PassengerAncillaryBuilder {

    public static String ancillaryDocNumberCountNine = "0010";
    public static String ancillaryDocNumberCountTen = "001";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public Set<Ancillary> build(TicketReceiptRsRow ticketReceiptRsRow) {

        String ancillaryDocNumber = ticketReceiptRsRow.getAnclryDocNbr();
        Set<Ancillary> ancillaryList = new HashSet<>();

        if (StringUtils.isNotBlank(ancillaryDocNumber)) {
            Ancillary ancillary = new Ancillary();
            int ancillaryDocNumberCount = ancillaryDocNumber.length();
            if (ancillaryDocNumberCount == 9) {
                ancillaryDocNumber = ancillaryDocNumberCountNine.concat(ancillaryDocNumber);
                ancillary.setAnclryDocNbr(ancillaryDocNumber);
            } else if (ancillaryDocNumberCount == 10) {
                ancillaryDocNumber = ancillaryDocNumberCountTen.concat(ancillaryDocNumber);
                ancillary.setAnclryDocNbr(ancillaryDocNumber);
            }

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
            if (ancillaryPriceCurrencyAmount != null) {
                BigDecimal anclryTaxCurrencyAmount = new BigDecimal(anclrySalesCurrencyAmount).subtract(new BigDecimal(ancillaryPriceCurrencyAmount)).setScale(2, RoundingMode.CEILING);
                ancillary.setAnclryTaxCurrencyAmount(anclryTaxCurrencyAmount.toString());
            }


            ancillaryList.add(ancillary);
        }
        return ancillaryList;
    }
}
