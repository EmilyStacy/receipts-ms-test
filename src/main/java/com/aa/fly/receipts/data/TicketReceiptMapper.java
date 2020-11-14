package com.aa.fly.receipts.data;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.fly.receipts.data.builder.PassengerBuilder;
import com.aa.fly.receipts.data.builder.PnrHeaderBuilder;
import com.aa.fly.receipts.data.builder.PnrSegmentBuilder;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;

@Component
public class TicketReceiptMapper {

    @Autowired
    private PnrHeaderBuilder pnrHeaderBuilder;
    @Autowired
    private PnrSegmentBuilder pnrSegmentBuilder;    
    @Autowired
    private PassengerBuilder passengerBuilder;    
    
    public TicketReceipt mapTicketReceipt(List<TicketReceiptRsRow> ticketReceiptRsRowList) {

        TicketReceipt ticketReceiptReturn = null;
        int rowCount = 0;
        String lastDepartureDateTime = "";
        String currentDepartureDateTime = null;

        Iterator<TicketReceiptRsRow> iterator = ticketReceiptRsRowList.iterator();
        TicketReceiptRsRow ticketReceiptRsRow = null;

        while (iterator.hasNext()) {
        	ticketReceiptRsRow = iterator.next();
        	
            if (rowCount == 0) {
            	ticketReceiptReturn = pnrHeaderBuilder.build(new TicketReceipt(), ticketReceiptRsRow);
            	ticketReceiptReturn = passengerBuilder.build(ticketReceiptReturn, ticketReceiptRsRow);
            }

            currentDepartureDateTime = Objects.requireNonNull(ticketReceiptRsRow.getSegDeptDt().toString())
            		.concat(Objects.requireNonNull(ticketReceiptRsRow.getSegDeptTm()));

            if (!lastDepartureDateTime.equals(currentDepartureDateTime))
            {
            	ticketReceiptReturn = pnrSegmentBuilder.build(ticketReceiptReturn, ticketReceiptRsRow, rowCount);
                lastDepartureDateTime = currentDepartureDateTime;
            }
            
            rowCount++;
        }
        return ticketReceiptReturn;
    }
}
