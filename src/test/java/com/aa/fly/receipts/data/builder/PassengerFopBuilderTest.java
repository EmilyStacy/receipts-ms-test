package com.aa.fly.receipts.data.builder;

import com.aa.fly.receipts.data.CreditCardAliasRepository;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;
import com.aa.fly.receipts.util.Constants;
import com.aa.fly.receipts.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.omg.CORBA.Any;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PassengerFopBuilderTest {

    @Mock
    private CreditCardAliasRepository creditCardAliasRepository;

    @InjectMocks
    private PassengerFopBuilder passengerFopBuilder= new PassengerFopBuilder();

    private TicketReceipt ticketReceipt;
    private TicketReceiptRsRow ticketReceiptRsRow = null;

    @Test
    public void testBuild_FOP() throws Exception {
        this.ticketReceiptRsRow= Utils.mockTicketReceiptRsRow();
        Mockito.when(creditCardAliasRepository.getCreditCardAliasMap()).thenReturn(fopTypeMap());
        this.ticketReceipt = passengerFopBuilder.build(mockTicketReceipt(),this.ticketReceiptRsRow);
        assertNotNull(this.ticketReceipt);
        assertNotNull(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments());
        assertEquals(1, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().size());
        assertEquals(Constants.FOP_ISSUE_DT, Constants.dateFormat.format(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopIssueDate()));
        assertEquals(Constants.FOP_AMT, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
        assertEquals(Constants.FOP_CURRTYPE_CD, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopCurrencyCode());
        assertEquals(Constants.FOP_TYPE_CD, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeCode());
        assertEquals(Constants.FOP_ACCTNBR_LAST4, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAccountNumberLast4());
        assertEquals(Constants.FOP_TYPE_DESCRIPTION, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());

    }

    public Map<String, String> fopTypeMap() {
        Map<String, String> fopTypeMap = new HashMap<>();
        fopTypeMap.put("CCBA", "Visa");
        fopTypeMap.put("CCVI", "Visa");
        return fopTypeMap;
    }

    private TicketReceipt mockTicketReceipt() throws ParseException {

        TicketReceipt receipt = new TicketReceipt();
        PassengerDetail passengerDetail = new PassengerDetail();
        passengerDetail.setTicketNumber(Constants.TICKET_NBR);
        passengerDetail.setFirstName(Constants.FIRST_NM);
        passengerDetail.setLastName(Constants.LAST_NM);
        passengerDetail.setAdvantageNumber(Constants.AADVANT_NBR);
        passengerDetail.setLoyaltyOwnerCode(Constants.LYLTY_OWN_CD);
        passengerDetail.setFormOfPayments(new ArrayList<>());
        receipt.getPassengerDetails().add(passengerDetail);
        return receipt;
    }

}