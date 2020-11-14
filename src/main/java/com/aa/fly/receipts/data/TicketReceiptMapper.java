package com.aa.fly.receipts.data;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.data.builder.PassengerBuilder;
import com.aa.fly.receipts.data.builder.PnrHeaderBuilder;
import com.aa.fly.receipts.data.builder.PnrSegmentBuilder;
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
    
    public TicketReceipt mapTicketReceipt(List<TicketReceiptRsRow> ticketReceiptRsRowList) {

        int rowCount = 0;
        TicketReceipt ticketReceiptReturn = null;
        String firstDepartureDateTime = null;
        String lastDepartureDateTime = null;
        String currentDepartureDateTime = null;

        Iterator<TicketReceiptRsRow> iterator = ticketReceiptRsRowList.iterator();
        TicketReceiptRsRow ticketReceiptRsRow = null;

        while (iterator.hasNext()) {
        	ticketReceiptRsRow = iterator.next();
        	
//            if (StringUtils.isNotBlank(ticketReceiptRsRow.getTcnBulkInd())) {
//                throw new BulkTicketException("BulkTicket");
//            }
        	
        	// Building data from first row.
            if (rowCount == 0) {
            	ticketReceiptReturn = pnrHeaderBuilder.build(new TicketReceipt(), ticketReceiptRsRow);
            	ticketReceiptReturn = passengerBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
            	ticketReceiptReturn = pnrSegmentBuilder.build(ticketReceiptReturn, ticketReceiptRsRow, rowCount);
            	
            	// Build Fare only need once here.
            	
            	lastDepartureDateTime = Objects.requireNonNull(ticketReceiptRsRow.getSegDeptDt().toString())
                		.concat(Objects.requireNonNull(ticketReceiptRsRow.getSegDeptTm()));
            	firstDepartureDateTime = lastDepartureDateTime;
            }

            currentDepartureDateTime = Objects.requireNonNull(ticketReceiptRsRow.getSegDeptDt().toString())
            		.concat(Objects.requireNonNull(ticketReceiptRsRow.getSegDeptTm()));
            
        	// Building data from every row in the first segment.
            if (firstDepartureDateTime.equals(currentDepartureDateTime)) {
            	// Build Tax Item (Set).
            	// build FOPs --> based on, FormOfPaymentKey formOfPaymentKey = new FormOfPaymentKey(fopSequenceId, fopTypeCode);
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
}
