package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.service.DataBuilderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class PassengerFareTaxFeeBuilderTest {

    private DataBuilderService passengerFareTaxFeeBuilder = new PassengerFareTaxFeeBuilder();

    static final String BASE_FARE_AMT = "394.42";
    static final String BASE_FARE_CURR_CODE = "USD";
    static final String TOTAL_FARE_AMT = "452.80";
    static final String TAX_FARE_AMT = "58.38";

    private TicketReceipt ticketReceipt = new TicketReceipt();
    private TicketReceiptRsRow ticketReceiptRsRow = null;

    @Test
    public void testBuild_PassengerFareTaxFee() throws Exception {
        this.mockTicketReceiptRsRow();
        DataBuilderService passengerBuilder = new PassengerBuilder();
        this.ticketReceipt = passengerBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);
        this.ticketReceipt = passengerFareTaxFeeBuilder.build(this.ticketReceipt, this.ticketReceiptRsRow);

        assertNotNull(this.ticketReceipt);
        assertNotNull(this.ticketReceipt.getPassengerDetails());
        assertEquals(1, this.ticketReceipt.getPassengerDetails().size());
        assertNotNull(this.ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees());
        assertEquals(BASE_FARE_AMT, this.ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareAmount());
        assertEquals(BASE_FARE_CURR_CODE, this.ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getBaseFareCurrencyCode());
        assertEquals(TOTAL_FARE_AMT, this.ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTotalFareAmount());
        assertEquals(TAX_FARE_AMT, this.ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees().getTaxFareAmount());
    }

    private void mockTicketReceiptRsRow() throws ParseException {
        this.ticketReceiptRsRow = TicketReceiptRsRow.builder()
                .fnumFareAmt(BASE_FARE_AMT)
                .fnumFareCurrTypeCd(BASE_FARE_CURR_CODE)
                .fareTdamAmt(TOTAL_FARE_AMT)
                .taxAmt(TAX_FARE_AMT)
                .build();
    }
}
