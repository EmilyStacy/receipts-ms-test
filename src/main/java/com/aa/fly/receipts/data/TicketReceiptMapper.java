package com.aa.fly.receipts.data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.data.builder.PassengerAncillaryBuilder;
import com.aa.fly.receipts.data.builder.PassengerBuilder;
import com.aa.fly.receipts.data.builder.PassengerFareTaxFeeBuilder;
import com.aa.fly.receipts.data.builder.PassengerFopBuilder;
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
    @Autowired
    private PassengerFopBuilder passengerFopBuilder;    
//    @Autowired
//    private PassengerTotalAdjuster passengerTotalAdjuster;    
        
    public TicketReceipt mapTicketReceipt(List<TicketReceiptRsRow> ticketReceiptRsRowList) {

        int rowCount = 0;
        TicketReceipt ticketReceiptReturn = null;
        String firstDepartureDateTime = null;
        String lastDepartureDateTime = null;
        String currentDepartureDateTime = null;
        Set<FormOfPaymentKey> fopKeys = new HashSet<>();
        Set<String> anclryDocNums = new HashSet<>();

        Iterator<TicketReceiptRsRow> iterator = ticketReceiptRsRowList.iterator();
        TicketReceiptRsRow ticketReceiptRsRow = null;

        while (iterator.hasNext()) {
        	ticketReceiptRsRow = iterator.next();
        	
            if (StringUtils.isNotBlank(ticketReceiptRsRow.getTcnBulkInd())) {
                throw new BulkTicketException("BulkTicket");
            }
            
        	FormOfPaymentKey formOfPaymentKey = new FormOfPaymentKey(
            		ticketReceiptRsRow.getFopSeqId(), ticketReceiptRsRow.getFopTypeCd());

        	currentDepartureDateTime = Objects.requireNonNull(ticketReceiptRsRow.getSegDeptDt().toString())
            		.concat(Objects.requireNonNull(ticketReceiptRsRow.getSegDeptTm()));
            
        	// Building data from first row.
            if (rowCount == 0) {
            	ticketReceiptReturn = pnrHeaderBuilder.build(new TicketReceipt(), ticketReceiptRsRow);
            	ticketReceiptReturn = passengerBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
            	ticketReceiptReturn = pnrSegmentBuilder.build(ticketReceiptReturn, ticketReceiptRsRow, rowCount);
            	ticketReceiptReturn = passengerFareTaxFeeBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
            	ticketReceiptReturn = passengerFopBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
                fopKeys.add(formOfPaymentKey);

            	lastDepartureDateTime = currentDepartureDateTime;
            	firstDepartureDateTime = lastDepartureDateTime;
            }
            
        	// Building data from every row in the first segment.
            if (firstDepartureDateTime.equals(currentDepartureDateTime)) {
            	
            	// Build Passenger Ticket FOP if not already
                if (!fopKeys.contains(formOfPaymentKey) && isMappingFormOfPayment(ticketReceiptRsRow.getFopTypeCd())) {
                	
                	ticketReceiptReturn = passengerFopBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
                    fopKeys.add(formOfPaymentKey);

                    List<FormOfPayment> formOfPayments = this.adjustFormOfPaymentsIfExchanged(
                			ticketReceiptReturn.getPassengerDetails().get(0).getFormOfPayments());
                    
                	ticketReceiptReturn.getPassengerDetails().get(0).setFormOfPayments(formOfPayments);
                }
                
            	// Build Tax Item (Set).

            	// Build Passenger Ancillary FOP if not already
//                if (!ticketReceiptRsRow.getAnclryDocNbr().isEmpty() && 
//                		!anclryDocNums.contains(ticketReceiptRsRow.getAnclryDocNbr())) {
//                	// call PassengerAncillaryFopBuilder
//                		// call PassengerAncillaryBuilder from PassengerAncillaryFopBuilder?
//                	
//                    anclryDocNums.add(ticketReceiptRsRow.getAnclryDocNbr());
//                }
            }

        	// Building data from the row when segment changed.
            if (!lastDepartureDateTime.equals(currentDepartureDateTime))
            {
            	ticketReceiptReturn = pnrSegmentBuilder.build(ticketReceiptReturn, ticketReceiptRsRow, rowCount);
                lastDepartureDateTime = currentDepartureDateTime;
            }
            
            rowCount++;
        }
        
        // One time adjustments, passing ticketReceiptReturn
        
        // PassengerTaxZPAdjuster.adjust(ticketReceiptReturn);
        // PassengerTaxXFAdjuster.adjust(ticketReceiptReturn);
        // passengerTotalAdjuster.adjust(ticketReceiptReturn);
        
        return ticketReceiptReturn;
    }
    
    // Move to PassengerFopBuilder
    private boolean isMappingFormOfPayment(String fopTypeCode) {
        return fopTypeCode.startsWith("CC") || fopTypeCode.startsWith("CA");
    }
    
    private List<FormOfPayment> adjustFormOfPaymentsIfExchanged(List<FormOfPayment> formOfPayments) {
        boolean isExchange = formOfPayments.stream().anyMatch(f -> "EF".equals(f.getFopTypeCode()) || "EX".equals(f.getFopTypeCode()));
        if (isExchange) {
            formOfPayments = formOfPayments.stream().filter(f -> f.getFopAmount() != null && BigDecimal.valueOf(Double.valueOf(f.getFopAmount())).compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.toList());
            formOfPayments.stream().forEach(f -> f.setFopTypeDescription("Exchange - " + f.getFopTypeDescription()));
        }

        return formOfPayments;
    }    
}
