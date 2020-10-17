package com.aa.fly.receipts.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.NoCostDetailsFoundException;

@Repository
@Transactional(readOnly = true)
public class TicketReceiptRepository {

    public static final String JOIN = "JOIN ";
    public static final String LEFT_JOIN = "LEFT JOIN ";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    @Autowired
    private TicketReceiptMapper ticketReceiptMapper;

    @Autowired
    private CostDetailsMapper costDetailsMapper;

    public TicketReceipt findTicketReceiptByTicketNumber(SearchCriteria criteria) {
        String ticketNumberSc = criteria.getTicketNumber().trim();
        String ticketNumber = (ticketNumberSc.length() == 13) ? ticketNumberSc.substring(3) : ticketNumberSc;
        String lastName = criteria.getLastName().toUpperCase().trim();
        String firstName = criteria.getFirstName().toUpperCase().trim() + '%';
        String departureDate = criteria.getDepartureDate();

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
                .append("AND DEP_DT = to_date(?, 'MM/DD/YYYY') \n")
                .append("AND UPPER(TRIM(FIRST_NM)) LIKE ? \n")
                .append("AND UPPER(TRIM(LAST_NM)) = ? \n")
                .append("ORDER BY SEG_DEPT_DT, SEG_DEPT_TM, FOP_SEQ_ID, TAX_CD_SEQ_ID \n")
                .toString();
        
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, ticketNumber, departureDate, firstName, lastName);
        return ticketReceiptMapper.mapTicketReceipt(sqlRowSet);
    }

    public PassengerDetail findCostDetailsByTicketNumber(SearchCriteria criteria, PassengerDetail passengerDetail) {

        String lastName = criteria.getLastName().toUpperCase().trim();
        String firstName = criteria.getFirstName().toUpperCase().trim() + '%';
        String departureDate = criteria.getDepartureDate();
        String ticketNumberSc = criteria.getTicketNumber().trim();
        String ticketNumber10 = (ticketNumberSc.length() == 13) ? ticketNumberSc.substring(3) : ticketNumberSc;
        String ticketNumber13 = (ticketNumberSc.length() == 13) ? ticketNumberSc : new StringBuilder("001").append(ticketNumberSc).toString();

        String sql = new StringBuilder("\nSELECT ")
                .append(" odtkt.OD_TICKET_AIRLN_ACCT_CD AS AIRLN_ACCT_CD \n")
                .append("    , odtkt.OD_TICKET_NBR AS TICKET_NBR \n")
                .append("    , odtkt.OD_TICKET_ISSUE_DT AS TICKET_ISSUE_DT \n")
                .append("    , odtkt.OD_LOCAL_DEP_DT AS DEP_DT \n")
                .append("    , tcust.PNR_PAX_FIRST_NM AS FIRST_NM \n")
                .append("    , tcust.PNR_PAX_LAST_NM AS LAST_NM \n")
                .append("    , rcptfop.ISSUE_DT AS FOP_ISSUE_DT \n")
                .append("    , rcptfop.FOP_TYPE_CD \n")
                .append("    , rcptfop.FOP_AMT \n")
                .append("    , rcptfop.FOP_SEQ_ID \n")
                .append("    , right(trim(rcptfop.FOP_ACCT_NBR), 4) AS FOP_ACCT_NBR_LAST4 \n")
                .append("    , rcptfop.FOP_CURR_TYPE_CD \n")
                .append("    , rcptfare.FNUM_FARE_AMT \n")
                .append("    , rcptfare.FNUM_FARE_CURR_TYPE_CD \n")
                .append("    , rcptfare.EQFN_FARE_AMT \n")
                .append("    , rcptfare.EQFN_FARE_CURR_TYPE_CD \n")
                .append("    , rcptfare.FARE_TDAM_AMT \n")
                .append("    , rcptfare.TCN_BULK_IND \n")

                .append("    , rcpttax.TAX_CD_SEQ_ID \n")
                .append("    , rcpttax.TAX_CD \n")
                .append("    , rcpttax.CITY_CD \n")
                .append("    , rcpttax.TAX_AMT \n")
                .append("    , rcpttax.TAX_CURR_TYPE_CD \n")

                .append("    , anclry.ANCLRY_SLS_DOC_NBR AS ANCLRY_DOC_NBR \n")
                .append("    , anclry.ANCLRY_SLS_ISSUE_DT AS ANCLRY_ISSUE_DT \n")
                .append("    , anclry.ANCLRY_PROD_CD \n")
                .append("    , anclname.ANCLRY_PROD_COMERCL_NM AS ANCLRY_PROD_NM \n")
                .append("    , anclry.ANCLRY_PRD_PRICE_LCL_CRNCY_AMT AS ANCLRY_PRICE_LCL_CURNCY_AMT \n")
                .append("    , anclry.ANCLRY_PRD_PRICE_LCL_CRNCY_CD AS ANCLRY_PRICE_LCL_CURNCY_CD \n")
                .append("    , anclry.ANCLRY_PROD_SLS_AMT AS ANCLRY_SLS_CURNCY_AMT \n")
                .append("    , anclry.ANCLRY_PROD_SLS_CURNCY_CD AS ANCLRY_SLS_CURNCY_CD \n")
                .append("    , odtktcpn.SEG_DEP_AIRPRT_IATA_CD AS SEG_DEPT_ARPRT_CD \n")
                .append("    , odtktcpn.SEG_ARVL_AIRPRT_IATA_CD AS SEG_ARVL_ARPRT_CD \n")
                .append("    , anclryfop.FOP_TYPE_CD AS ANCLRY_FOP_TYPE_CD \n")
                .append("    , anclryfop.FOP_AMT AS ANCLRY_FOP_AMT \n")
                .append("    , Right(trim(anclryfop.FOP_ACCT_NBR), 4) AS ANCLRY_FOP_ACCT_NBR_LAST4 \n")
                .append("    , anclryfop.FOP_CURR_TYPE_CD AS ANCLRY_FOP_CURR_TYPE_CD \n")

                .append("FROM \n")
                .append("      ").append(ticketSchemaName).append(".OD_TICKET odtkt \n")
                .append(JOIN).append(ticketSchemaName).append(".TICKET_CUSTOMER tcust \n")
                .append("ON odtkt.OD_TICKET_NBR = tcust.TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = tcust.TICKET_ISSUE_DT \n")
                .append(JOIN).append(ticketSchemaName).append(".TCN_RECEIPT_FOP rcptfop \n")
                .append("ON odtkt.OD_TICKET_ISSUE_DT = rcptfop.ISSUE_DT \n")
                .append(JOIN).append(ticketSchemaName).append(".TCN_RECEIPT_FARE rcptfare \n")
                .append("ON rcptfop.DOC_NBR = rcptfare.DOC_NBR AND rcptfop.ISSUE_DT = rcptfare.ISSUE_DT \n")
                .append(JOIN).append(ticketSchemaName).append(".TCN_RECEIPT_TAX rcpttax \n")
                .append("ON rcptfop.DOC_NBR = rcpttax.DOC_NBR AND rcptfop.ISSUE_DT = rcpttax.ISSUE_DT \n")
                .append(LEFT_JOIN).append(ticketSchemaName).append(".ANCLRY_SLS_DOC_ITEM anclry \n")
                .append("ON odtkt.OD_TICKET_NBR = anclry.TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = anclry.TICKET_ISSUE_DT \n")
                .append(LEFT_JOIN).append(ticketSchemaName).append(".ANCLRY_SLS_PROD anclname \n")
                .append("ON anclry.ANCLRY_PROD_CD = anclname.ANCLRY_PROD_ACCT_CD \n")
                .append(LEFT_JOIN).append(ticketSchemaName).append(".OD_TICKET_TRAVEL_COUPON odtktcpn \n")
                .append("ON anclry.TICKET_NBR = odtktcpn.OD_TICKET_NBR AND anclry.TICKET_ISSUE_DT = odtktcpn.OD_TICKET_ISSUE_DT AND \r\n" +
                        "anclry.TICKET_COUPON_SEQ_NBR = odtktcpn.TICKET_COUPON_SEQ_NBR AND odtktcpn.OD_TYPE_CD = 'TRUE_OD' \n")
                .append(LEFT_JOIN).append(ticketSchemaName).append(".TCN_RECEIPT_FOP anclryfop \n")
                .append("ON anclry.ANCLRY_SLS_AIRLN_ACCT_CD || '0' || Cast(anclry.ANCLRY_SLS_DOC_NBR AS VARCHAR(9)) = anclryfop.DOC_NBR AND \r\n" +
                        "anclry.ANCLRY_SLS_ISSUE_DT = anclryfop.ISSUE_DT \n")
                .append(JOIN).append(ticketSchemaName).append(".TICKET  tkt \n")
                .append("ON odtkt.OD_TICKET_NBR = tkt.TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = tkt.TICKET_ISSUE_DT \n")
                .append("WHERE \n")
                .append("    odtkt.OD_SRC_SYS_CD = 'VCR' \n")
                .append("    AND odtkt.OD_TYPE_CD = 'TRUE_OD' \n")
                .append("    AND tcust.TICKET_NBR = ? \n")
                .append("    AND rcptfop.DOC_NBR = ? \n")
                .append("    AND odtkt.OD_LOCAL_DEP_DT = to_date(? , 'MM/DD/YYYY') \n")
                .append("    AND UPPER(TRIM(tcust.PNR_PAX_FIRST_NM)) LIKE ? \n")
                .append("    AND UPPER(TRIM(tcust.PNR_PAX_LAST_NM)) = ? \n")
                .append("ORDER BY rcptfop.ISSUE_DT, rcptfop.FOP_SEQ_ID, rcpttax.TAX_CD_SEQ_ID \n")
                .toString();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, ticketNumber10, ticketNumber13, departureDate,
                firstName, lastName);
        if (!sqlRowSet.isBeforeFirst()) {
            throw new NoCostDetailsFoundException("No cost details found for search criteria = " + criteria);
        }

        return costDetailsMapper.mapCostDetails(sqlRowSet, passengerDetail);
    }
}
