package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.util.Constants;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class PassengerFopBuilderTest {


    private PassengerFopBuilder passengerFopBuilder= new PassengerFopBuilder();
    private TicketReceipt ticketReceipt = new TicketReceipt();
    private TicketReceiptRsRow ticketReceiptRsRow = null;

    @Test
    public void testBuild_FOP() throws Exception {
        this.mockTicketReceiptRsRow();

        this.ticketReceipt = passengerFopBuilder.build(this.ticketReceipt,this.ticketReceiptRsRow);
        assertNotNull(this.ticketReceipt);
        assertNotNull(this.ticketReceipt.getFormOfPayments());
        assertEquals(1, this.ticketReceipt.getFormOfPayments().size());
        assertEquals(Constants.FOP_ISSUE_DT, Constants.dateFormat.format(this.ticketReceipt.getFormOfPayments().get(0).getFopIssueDate()));
        assertEquals(Constants.FOP_AMT, this.ticketReceipt.getFormOfPayments().get(0).getFopAmount());
        assertEquals(Constants.FOP_CURRTYPE_CD, this.ticketReceipt.getFormOfPayments().get(0).getFopCurrencyCode());
        assertEquals(Constants.FOP_TYPE_CD, this.ticketReceipt.getFormOfPayments().get(0).getFopTypeCode());
        assertEquals(Constants.FOP_ACCTNBR_LAST4, this.ticketReceipt.getFormOfPayments().get(0).getFopAccountNumberLast4());
        assertEquals(Constants.FOP_TYPE_DESCRIPTION, this.ticketReceipt.getFormOfPayments().get(0).getFopTypeDescription());

    }

    private void mockTicketReceiptRsRow() throws ParseException {
        this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
                .fopAcctNbrLast4(Constants.FOP_ACCTNBR_LAST4)
                .fopAmt(Constants.FOP_AMT)
                .fopCurrTypeCd(Constants.FOP_CURRTYPE_CD)
                .fopIssueDt(Constants.dateFormat.parse(Constants.FOP_ISSUE_DT))
                .fopTypeCd(Constants.FOP_TYPE_CD)
                .fopTypeDescription(Constants.FOP_TYPE_DESCRIPTION)
                .build();
    }
}