package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PassengerAncillaryBuilderTest {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @InjectMocks
    private PassengerAncillaryBuilder passengerAncillaryBuilder;

    @Test
    public void canaryTest() {
        assertNotNull(passengerAncillaryBuilder);
    }

    @Test
    public void testThatIfMosaicHasNoAncillariesThenTicketReceiptHasNoAncillariesAsWell() throws ParseException {
        TicketReceipt ticketReceipt = new TicketReceipt();
        TicketReceiptRsRow ticketReceiptRsRow = mockTicketReceiptRows();
        ticketReceipt = passengerAncillaryBuilder.build(mockTicketReceipt(ticketReceipt), ticketReceiptRsRow);
        assertFalse(CollectionUtils.isEmpty(ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getAncillaries()));

        Ancillary actualAncillary = ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getAncillaries().iterator().next();
        assertEquals(ticketReceiptRsRow.getAnclryDocNbr(), actualAncillary.getAnclryDocNbr());
        assertEquals(ticketReceiptRsRow.getAnclryIssueDt(), dateFormat.parse(actualAncillary.getAnclryIssueDate()));
        assertEquals(ticketReceiptRsRow.getAnclryProdNm() + " (MIA - STT)", actualAncillary.getAnclryProdName());
        assertEquals(ticketReceiptRsRow.getAnclryProdCd(), actualAncillary.getAnclryProdCode());
        assertEquals(ticketReceiptRsRow.getAnclryPriceLclCurncyAmt(), actualAncillary.getAnclryPriceCurrencyAmount());
        assertEquals(ticketReceiptRsRow.getAnclryPriceLclCurncyCd(), actualAncillary.getAnclryPriceCurrencyCode());
        assertEquals(ticketReceiptRsRow.getAnclrySlsCurncyAmt(), actualAncillary.getAnclrySalesCurrencyAmount());
        assertEquals(ticketReceiptRsRow.getAnclrySlsCurncyCd(), actualAncillary.getAnclrySalesCurrencyCode());
        assertEquals("968.22", actualAncillary.getAnclryTaxCurrencyAmount());
    }

    private TicketReceipt mockTicketReceipt(TicketReceipt ticketReceipt) {
        ticketReceipt.setPassengerDetails(mockPassengerDetails());
        return ticketReceipt;
    }

    private List<PassengerDetail> mockPassengerDetails() {
        List<PassengerDetail> passengerDetails = new ArrayList<>();
        PassengerDetail passengerDetail = new PassengerDetail();
        passengerDetail.setFormOfPayments(mockFormOfPayments());
        passengerDetails.add(passengerDetail);
        return passengerDetails;
    }

    private List<FormOfPayment> mockFormOfPayments() {
        List<FormOfPayment> formOfPayments = new ArrayList<>();
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayments.add(formOfPayment);
        return formOfPayments;
    }

    private TicketReceiptRsRow mockTicketReceiptRows() throws ParseException {
        return TicketReceiptRsRow.builder()
                .anclryDocNbr("616888085")
                .anclryIssueDt(dateFormat.parse("03/19/2020"))
                .anclryProdCd("88")
                .anclryProdNm("PREFERRED SEATS")
                .segDeptArprtCd("MIA")
                .segArvlArprtCd("STT")
                .anclryPriceLclCurncyAmt("31.78")
                .anclryPriceLclCurncyCd("USD")
                .anclrySlsCurncyAmt("1000.00")
                .anclrySlsCurncyCd("USD")
                .anclryFopAcctNbrLast4("1699")
                .anclryFopAmt("568.29")
                .anclryFopCurrTypeCd("USD2")
                .anclryFopTypeCd("CCIK")
                .build();

    }

}
