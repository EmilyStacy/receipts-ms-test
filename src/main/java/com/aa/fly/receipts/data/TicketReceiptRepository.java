package com.aa.fly.receipts.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.domain.TicketReceiptRsRow;

@Repository
@Transactional(readOnly = true)
public class TicketReceiptRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    @Autowired
    private TicketReceiptRsExtracter ticketReceiptRsExtracter;
    
    @Autowired
    private TicketReceiptMapper ticketReceiptMapper;

//    @Autowired
//    private CostDetailsMapper costDetailsMapper;

    public TicketReceipt findTicketReceiptByTicketNumber(SearchCriteria criteria) {
        String ticketNumber = criteria.getTicketNumber();
        String lastName = criteria.getLastName().toUpperCase().trim();
//        String firstName = criteria.getFirstName().toUpperCase().trim() + '%';
//        String departureDate = criteria.getDepartureDate();

        String sql = new StringBuilder("\nSELECT ")
                // ================= header ==============================
                .append("    AIRLN_ACCT_CD, \n")
                .append("    TICKET_NBR, \n")
                .append("    TICKET_ISSUE_DT, \n")
                .append("    DEP_DT, \n")
                .append("    FIRST_NM, \n")
                .append("    LAST_NM, \n")
                .append("    ORG_ATO_CD, \n")
                .append("    DEST_ATO_CD, \n")
                .append("    PNR, \n")
                .append("    AADVANT_NBR, \n")
                .append("    LYLTY_OWN_CD, \n")
                // =================== trip details =======================
                .append("    SEG_DEPT_DT, \n")
                .append("    SEG_DEPT_TM, \n")
                .append("    SEG_DEPT_ARPRT_CD, \n")
                .append("    SEG_ARVL_DT, \n")
                .append("    SEG_ARVL_TM, \n")
                .append("    SEG_ARVL_ARPRT_CD, \n")
                .append("    SEG_COUPON_STATUS_CD, \n")
                .append("    SEG_OPERAT_CARRIER_CD, \n")
                .append("    FLIGHT_NBR, \n")
                .append("    BOOKING_CLASS, \n")
                .append("    FARE_BASE, \n")
                .append("    COUPON_SEQ_NBR, \n")
                // =================== cost - fop =======================
                .append("    FOP_ISSUE_DT, \n")
                .append("    FOP_TYPE_CD, \n")
                .append("    FOP_AMT, \n")
                .append("    FOP_SEQ_ID, \n")
                .append("    FOP_ACCT_NBR_LAST4, \n")
                .append("    FOP_CURR_TYPE_CD, \n")
                // =================== cost - fare =======================
                .append("    FNUM_FARE_AMT, \n")
                .append("    FNUM_FARE_CURR_TYPE_CD, \n")
                .append("    EQFN_FARE_AMT, \n")
                .append("    EQFN_FARE_CURR_TYPE_CD, \n")
                .append("    FARE_TDAM_AMT, \n")
                .append("    TCN_BULK_IND, \n")
                // =================== cost - tax =======================
                .append("    TAX_CD_SEQ_ID, \n")
                .append("    TAX_CD, \n")
                .append("    CITY_CD, \n")
                .append("    TAX_AMT, \n")
                .append("    TAX_CURR_TYPE_CD, \n")
                // =================== cost - ancillary =======================
                .append("    ANCLRY_DOC_NBR, \n")
                .append("    ANCLRY_ISSUE_DT, \n")
                .append("    ANCLRY_PROD_CD, \n")
                .append("    ANCLRY_PROD_NM, \n")
                .append("    ANCLRY_PRICE_LCL_CURNCY_AMT, \n")
                .append("    ANCLRY_PRICE_LCL_CURNCY_CD, \n")
                .append("    ANCLRY_SLS_CURNCY_AMT, \n")
                .append("    ANCLRY_SLS_CURNCY_CD, \n")
                .append("    ANCLRY_FOP_AMT, \n")
                .append("    ANCLRY_FOP_TYPE_CD, \n")
                .append("    ANCLRY_FOP_ACCT_NBR_LAST4, \n")
                .append("    ANCLRY_FOP_CURR_TYPE_CD \n")
                .append("FROM ").append( ticketSchemaName ).append(".TCN_CUST_RCPT \n")
                .append("WHERE \n")
                .append("TICKET_NBR = ? \n")
//                .append("AND DEP_DT = to_date(?, 'MM/DD/YYYY') \n")
//                .append("AND UPPER(TRIM(FIRST_NM)) LIKE ? \n")
                .append("AND LAST_NM = ? \n")
                .append("ORDER BY SEG_DEPT_DT, SEG_DEPT_TM, FOP_SEQ_ID, TAX_CD_SEQ_ID, ANCLRY_ISSUE_DT, ANCLRY_DOC_NBR, ANCLRY_PRICE_LCL_CURNCY_AMT DESC \n")
                .toString();
        
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, ticketNumber, lastName);
        
        final List<TicketReceiptRsRow> ticketReceiptRsRowList = ticketReceiptRsExtracter.extract(sqlRowSet);
        
        TicketReceipt ticketReceipt = null;
        
        if (CollectionUtils.isEmpty(ticketReceiptRsRowList)) {
        	ticketReceipt = new TicketReceipt();
        } else {
            ticketReceipt = ticketReceiptMapper.mapTicketReceipt(ticketReceiptRsRowList);

//            sqlRowSet.beforeFirst();
//            PassengerDetail passengerDetail = costDetailsMapper.mapCostDetails(sqlRowSet, ticketReceipt.getPassengerDetails().get(0));
//            ticketReceipt.getPassengerDetails().set(0, passengerDetail);
        }

        return ticketReceipt;
    }
}
