package com.aa.fly.receipts.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.data.builder.PassengerBuilder;
import com.aa.fly.receipts.data.builder.PassengerFareTaxFeeBuilder;
import com.aa.fly.receipts.data.builder.PnrHeaderBuilder;
import com.aa.fly.receipts.data.builder.PnrSegmentBuilder;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.FormOfPaymentKey;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.exception.BulkTicketException;

@Component
public class TicketReceiptMapper {

    @Autowired
    private PnrHeaderBuilder pnrHeaderBuilder;
    @Autowired
    private PnrSegmentBuilder pnrSegmentBuilder;    
    @Autowired
    private PassengerBuilder passengerBuilder;    
    @Autowired
    private PassengerFareTaxFeeBuilder passengerFareTaxFeeBuilder;    
    
    public TicketReceipt mapTicketReceipt(List<TicketReceiptRsRow> ticketReceiptRsRowList) {

        int rowCount = 0;
        TicketReceipt ticketReceiptReturn = null;
        String firstDepartureDateTime = null;
        String lastDepartureDateTime = null;
        String currentDepartureDateTime = null;
        Set<FormOfPaymentKey> fopKeys = new HashSet<>();
        List<FormOfPayment> formOfPayments = new ArrayList<>();

        Iterator<TicketReceiptRsRow> iterator = ticketReceiptRsRowList.iterator();
        TicketReceiptRsRow ticketReceiptRsRow = null;

        while (iterator.hasNext()) {
        	ticketReceiptRsRow = iterator.next();
        	
            if (StringUtils.isNotBlank(ticketReceiptRsRow.getTcnBulkInd())) {
                throw new BulkTicketException("BulkTicket");
            }
        	
        	// Building data from first row.
            if (rowCount == 0) {
            	ticketReceiptReturn = pnrHeaderBuilder.build(new TicketReceipt(), ticketReceiptRsRow);
            	ticketReceiptReturn = passengerBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
            	ticketReceiptReturn = pnrSegmentBuilder.build(ticketReceiptReturn, ticketReceiptRsRow, rowCount);
            	ticketReceiptReturn = passengerFareTaxFeeBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
            	
            	lastDepartureDateTime = Objects.requireNonNull(ticketReceiptRsRow.getSegDeptDt().toString())
                		.concat(Objects.requireNonNull(ticketReceiptRsRow.getSegDeptTm()));
            	firstDepartureDateTime = lastDepartureDateTime;
            }

            currentDepartureDateTime = Objects.requireNonNull(ticketReceiptRsRow.getSegDeptDt().toString())
            		.concat(Objects.requireNonNull(ticketReceiptRsRow.getSegDeptTm()));
            
        	// Building data from every row in the first segment.
            if (firstDepartureDateTime.equals(currentDepartureDateTime)) {
            	// FOP building setup
	            //FormOfPaymentKey formOfPaymentKey = new FormOfPaymentKey(
	            		//ticketReceiptRsRow.getFopSeqId(), ticketReceiptRsRow.getFopTypeCd());
	            
                //if (!fopKeys.contains(formOfPaymentKey) && isMappingFormOfPayment(ticketReceiptRsRow.getFopTypeCd())) {
                	// build FOPs
                	// ticketReceiptReturn = passengerFopBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);

                    //this.adjustFormOfPaymentsIfExchanged(ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments());
    	            //fopKeys.add(formOfPaymentKey);
                //}
                
            	// Build Tax Item (Set).
            	// Build Ancillary (Set) -> Add to set anclryDocNums.add(anclryDocNbr);
            	// if (anclryDocNbr not in anclryDocNums)
            	//   Build Ancillary FOPs.
            }
           
        	// Building data from the row when segment changed.
            if (!lastDepartureDateTime.equals(currentDepartureDateTime))
            {
            	ticketReceiptReturn = pnrSegmentBuilder.build(ticketReceiptReturn, ticketReceiptRsRow, rowCount);
                lastDepartureDateTime = currentDepartureDateTime;
            }
            
            rowCount++;
        }
        
        // passing ticketReceiptReturn
        
        // PassengerFopAdjuster
        // PassengerTotalAdjuster
        // PassengerTaxAdjuster
        
        return ticketReceiptReturn;
    }
    
    // Move to PassengerFopBuilder
//    private boolean isMappingFormOfPayment(String fopTypeCode) {
//        return fopTypeCode.startsWith("CC") || fopTypeCode.startsWith("CA");
//    }
//    
//    private List<FormOfPayment> adjustFormOfPaymentsIfExchanged(List<FormOfPayment> formOfPayments) {
//        boolean isExchange = formOfPayments.stream().anyMatch(f -> "EF".equals(f.getFopTypeCode()) || "EX".equals(f.getFopTypeCode()));
//        if (isExchange) {
//            formOfPayments = formOfPayments.stream().filter(f -> f.getFopAmount() != null && BigDecimal.valueOf(Double.valueOf(f.getFopAmount())).compareTo(BigDecimal.ZERO) > 0)
//                    .collect(Collectors.toList());
//            formOfPayments.stream().forEach(f -> f.setFopTypeDescription("Exchange - " + f.getFopTypeDescription()));
//        }
//
//        return formOfPayments;
//    }    
}
