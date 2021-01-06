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
import org.mockito.internal.util.reflection.FieldSetter;
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

    @Mock
    private Map<String, String> fopTypeMap;

    @InjectMocks
    private PassengerFopBuilder passengerFopBuilder = new PassengerFopBuilder();

    private TicketReceipt ticketReceipt;
    private TicketReceiptRsRow ticketReceiptRsRow = null;


    public Map<String, String> fopTypeMap() {
        Map<String, String> fopTypeMap = new HashMap<>();
        fopTypeMap.put("CCBA", "Visa");
        fopTypeMap.put("CCVI", "Visa");
        return fopTypeMap;
    }


    @Test
    public void testBuild_PassengerFOPBuilder() throws Exception {
        this.ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        Mockito.when(creditCardAliasRepository.getCreditCardAliasMap()).thenReturn(fopTypeMap());
        this.ticketReceipt = passengerFopBuilder.build(mockTicketReceipt(), this.ticketReceiptRsRow);
        assertNotNull(this.ticketReceipt);
        assertNotNull(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments());
        assertTrue(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).isTicketed());
        assertEquals(1, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().size());
        assertEquals(Constants.FOP_ISSUE_DATE, Constants.dateFormat.format(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopIssueDate()));
        assertEquals(Constants.FOP_AMOUNT, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
        assertEquals(Constants.FOP_CURRENCY_CODE, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopCurrencyCode());
        assertEquals(Constants.FOP_TYPE_CODE, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeCode());
        assertEquals(Constants.FOP_ACCOUNT_NUMBER_LAST4, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAccountNumberLast4());
        assertEquals(Constants.FOP_TYPE_DESCRIPTION, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());

    }

    @Test
    public void testBuild_PassengerFOPBuilder_getFormOfPaymentDescription_else() throws Exception {
        this.ticketReceiptRsRow = this.mockTicketReceiptRsRow_with_Acc_last4_null();
        FieldSetter.setField(passengerFopBuilder, passengerFopBuilder.getClass().getDeclaredField("fopTypeMap"), fopTypeMap);
        Mockito.when(creditCardAliasRepository.getCreditCardAliasMap()).thenReturn(fopTypeMap());
        this.ticketReceipt = passengerFopBuilder.build(mockTicketReceipt(), this.ticketReceiptRsRow);
        assertNotNull(this.ticketReceipt);
        assertNotNull(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments());
        assertEquals(1, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().size());
        assertNotEquals(Constants.FOP_TYPE_DESCRIPTION, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    }

    private TicketReceiptRsRow mockTicketReceiptRsRow_with_Acc_last4_null() throws ParseException {
        return TicketReceiptRsRow.builder()
                .fopAcctNbrLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4)
                .fopAmt(Constants.FOP_AMOUNT)
                .fopCurrTypeCd(Constants.FOP_CURRENCY_CODE)
                .fopIssueDt(Constants.dateFormat.parse(Constants.FOP_ISSUE_DATE))
                .fopTypeCd(Constants.FOP_TYPE_CODE)
                .fopAcctNbrLast4(Constants.FOP_ACCOUNT_NUMBER_LAST4_Null)

                .build();
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