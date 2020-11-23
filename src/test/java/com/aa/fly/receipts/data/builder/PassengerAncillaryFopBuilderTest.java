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
public class PassengerAncillaryFopBuilderTest {

    @Mock
    private CreditCardAliasRepository creditCardAliasRepository;

    @Mock
    private Map<String, String> fopTypeMap;

    @InjectMocks
    private PassengerAncillaryFopBuilder passengerAncillaryFopBuilder = new PassengerAncillaryFopBuilder();

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
        //Given
        this.ticketReceiptRsRow = Utils.mockTicketReceiptRsRow();
        //When
        Mockito.when(creditCardAliasRepository.getCreditCardAliasMap()).thenReturn(fopTypeMap());
        this.ticketReceipt = passengerAncillaryFopBuilder.build(mockTicketReceipt(), this.ticketReceiptRsRow);
        //Then
        assertNotNull(this.ticketReceipt);
        assertNotNull(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments());
        assertEquals(1, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().size());
        assertEquals(Constants.ANCLRY_ISSUE_DATE, Constants.dateFormat.format(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopIssueDate()));
        assertEquals(Constants.ANCLRY_FOP_AMOUNT, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAmount());
        assertEquals(Constants.ANCLRY_FOP_CURRENCY_CODE, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopCurrencyCode());
        assertEquals(Constants.ANCLRY_FOP_TYPE_CODE, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeCode());
        assertEquals(Constants.ANCLRY_FOP_ACCOUNT_NUMBER_LAST4, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopAccountNumberLast4());
        assertEquals(Constants.ANCLRY_FOP_TYPE_DESCRIPTION, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());

    }

    @Test
    public void testBuild_PassengerFOPBuilder_getFormOfPaymentDescription_else() throws Exception {
        //Given
        this.ticketReceiptRsRow = this.mockTicketReceiptRsRow_with_Acc_last4_null();
        FieldSetter.setField(passengerAncillaryFopBuilder, passengerAncillaryFopBuilder.getClass().getDeclaredField("fopTypeMap"), fopTypeMap);
        //When
        Mockito.when(creditCardAliasRepository.getCreditCardAliasMap()).thenReturn(fopTypeMap());
        this.ticketReceipt = passengerAncillaryFopBuilder.build(mockTicketReceipt(), this.ticketReceiptRsRow);
        //Then
        assertNotNull(this.ticketReceipt);
        assertNotNull(this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments());
        assertEquals(1, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().size());
        assertNotEquals(Constants.FOP_TYPE_DESCRIPTION, this.ticketReceipt.getPassengerDetails().get(0).getFormOfPayments().get(0).getFopTypeDescription());
    }

    private TicketReceiptRsRow mockTicketReceiptRsRow_with_Acc_last4_null() throws ParseException {
        return TicketReceiptRsRow.builder()
                .anclryFopAcctNbrLast4(Constants.ANCLRY_FOP_ACCOUNT_NUMBER_LAST4)
                .anclryFopAmt(Constants.ANCLRY_FOP_AMOUNT)
                .anclryFopCurrTypeCd(Constants.ANCLRY_FOP_CURRENCY_CODE)
                .anclryIssueDt(Constants.dateFormat.parse(Constants.ANCLRY_ISSUE_DATE))
                .anclryFopTypeCd(Constants.ANCLRY_FOP_TYPE_CODE)
                .anclryFopAcctNbrLast4("")

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
 //       passengerDetail.setFormOfPayments(new ArrayList<>());
        receipt.getPassengerDetails().add(passengerDetail);
        return receipt;
    }

}