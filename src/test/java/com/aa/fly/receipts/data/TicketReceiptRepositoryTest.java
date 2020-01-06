package com.aa.fly.receipts.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.AmountAndCurrency;
import com.aa.fly.receipts.domain.FormOfPayment;
import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketReceiptRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SqlRowSet resultSet;

    @Mock
    private TicketReceiptMapper ticketReceiptMapper;

    @Mock
    private CostDetailsMapper costDetailsMapper;

    @InjectMocks
    private TicketReceiptRepository receiptRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Test
    public void findTicketReceiptByTicketNumber() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        TicketReceipt ticketReceipt = ReceiptsMSDomainTest.getTicketReceipt();
        List<TicketReceipt> ticketReceiptList = new ArrayList<>();
        ticketReceiptList.add(ticketReceipt);
        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(ticketReceiptMapper.mapTicketReceipt(resultSet))
                .thenReturn(ticketReceipt);
        assertEquals("MRYMPT", receiptRepository.findTicketReceiptByTicketNumber(criteria).getPnr());

    }

    @Test
    public void findCostDetailsByTicketNumber() throws ParseException {
        SearchCriteria criteria = ReceiptsMSDomainTest.getSearchCriteriaWithTicketNumber();
        PassengerDetail passengerDetail = new PassengerDetail();

        when(jdbcTemplate.queryForRowSet(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(resultSet);
        when(resultSet.isBeforeFirst()).thenReturn(true);
        PassengerDetail passengerDetailExpected = getPassengerDetailWithCostDetails(passengerDetail);
        when(costDetailsMapper.mapCostDetails(resultSet, passengerDetail))
                .thenReturn(passengerDetailExpected);

        PassengerDetail passengerDetailActual = receiptRepository.findCostDetailsByTicketNumber(criteria, passengerDetail);

        assertTrue(passengerDetailExpected.getFormOfPayments().get(0).equals(passengerDetailActual.getFormOfPayments().get(0)));
    }

    private PassengerDetail getPassengerDetailWithCostDetails(PassengerDetail passengerDetail) throws ParseException {
        List<FormOfPayment> formOfPayments = new ArrayList<>();
        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setFopAccountNumberLast4("0006");
        formOfPayment.setFopIssueDate(dateFormat.parse("10/30/2019"));
        AmountAndCurrency fopAmountAndCurrency = new AmountAndCurrency("53628", "CAD2");
        formOfPayment.setFopAmount(fopAmountAndCurrency.getAmount());
        formOfPayment.setFopCurrencyCode(fopAmountAndCurrency.getCurrencyCode());
        formOfPayment.setFopTypeCode("CCBA");
        formOfPayment.setFopTypeDescription("Visa");
        formOfPayments.add(formOfPayment);
        passengerDetail.setFormOfPayments(formOfPayments);
        return passengerDetail;
    }
}
