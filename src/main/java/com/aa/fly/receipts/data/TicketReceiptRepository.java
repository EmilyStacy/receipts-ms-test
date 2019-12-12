package com.aa.fly.receipts.data;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.exception.ReceiptsMSException;

@Repository
public class TicketReceiptRepository {

    public static final String JOIN = "JOIN ";
    public static final String LEFT_JOIN = "LEFT JOIN ";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    @Autowired
    private TicketReceiptMapper ticketReceiptMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional(readOnly = true)
    public TicketReceipt findTicketReceiptByTicketNumber(SearchCriteria criteria) {
        String lastName = criteria.getLastName().toUpperCase().trim();
        String firstName = criteria.getFirstName().toUpperCase().trim() + '%';
        String departureDate = dateFormat.format(criteria.getDepartureDate());
        String ticketNumberSc = criteria.getTicketNumber().trim();
        String ticketNumber = (ticketNumberSc.length() == 13) ? ticketNumberSc.substring(3) : ticketNumberSc;

        String sql = new StringBuilder("SELECT ")
                // ================= header ==============================
                .append("    odtkt.OD_TICKET_AIRLN_ACCT_CD AS AIRLN_ACCT_CD, ")
                .append("    odtkt.OD_TICKET_NBR AS TICKET_NBR, ")
                .append("    odtkt.OD_TICKET_ISSUE_DT AS TICKET_ISSUE_DT, ")
                .append("    odtkt.OD_LOCAL_DEP_DT AS DEP_DT, ")
                .append("    tcust.PNR_PAX_FIRST_NM AS FIRST_NM, ")
                .append("    tcust.PNR_PAX_LAST_NM AS LAST_NM, ")
                .append("    odtkt.OD_ORIGIN_AIRPRT_IATA_CD AS ORG_ATO_CD, ")
                .append("    odtkt.OD_DESTNTN_AIRPRT_IATA_CD AS DEST_ATO_CD, ")
                .append("    tkt.PNR_LOCTR_ID AS PNR, ")
                .append("    frqtr.LYLTY_ACCT_ID AS AADVANT_NBR, ")
                .append("    frqtr.LYLTY_PGM_OWN_CD AS LYLTY_OWN_CD,")
                // =================== trip details =======================
                .append("    odtktcpn.SEG_LOCAL_DEP_DT AS SEG_DEPT_DT, ")
                .append("    odtktcpn.SEG_LOCAL_OUT_TM AS SEG_DEPT_TM, ")
                .append("    odtktcpn.SEG_DEP_AIRPRT_IATA_CD AS SEG_DEPT_ARPRT_CD, ")

                .append("    odtktcpn.SEG_LOCAL_ARVL_DT AS SEG_ARVL_DT, ")
                .append("    odtktcpn.SEG_LOCAL_IN_TM AS SEG_ARVL_TM, ")
                .append("    odtktcpn.SEG_ARVL_AIRPRT_IATA_CD AS SEG_ARVL_ARPRT_CD, ")

                .append("    tktcpn.FLOWN_OPERAT_FLIGHT_NBR AS FLIGHT_NBR, ")
                .append("    tktcpn.ACCT_FARE_CLASS_CD AS BOOKING_CLASS, ")
                .append("    tktcpn.FARE_BASE_CD AS FARE_BASE, ")
                .append("    odtktcpn.OD_TICKET_COUPON_SEQ_NBR AS COUPON_SEQ_NBR, ")
                .append("    odtktcpn.OPERAT_AIRLN_IATA_CD AS SEG_OPERAT_CARRIER_CD ")
                .append("FROM ").append(ticketSchemaName).append(".OD_TICKET odtkt ")
                .append(JOIN).append(ticketSchemaName).append(".TICKET_CUSTOMER tcust ")
                .append("ON odtkt.OD_TICKET_NBR = tcust.TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = tcust.TICKET_ISSUE_DT ")
                .append(JOIN).append(ticketSchemaName).append(".TICKET tkt ")
                .append("ON odtkt.OD_TICKET_NBR = tkt.TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = tkt.TICKET_ISSUE_DT ")
                .append(JOIN).append(ticketSchemaName).append(".OD_TICKET_TRAVEL_COUPON odtktcpn ")
                .append("ON odtkt.OD_TICKET_NBR = odtktcpn.OD_TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = odtktcpn.OD_TICKET_ISSUE_DT ")
                .append(JOIN).append(ticketSchemaName).append(".TICKET_COUPON tktcpn ")
                .append("ON odtktcpn.OD_TICKET_NBR = tktcpn.TICKET_NBR AND odtktcpn.OD_TICKET_ISSUE_DT = tktcpn.TICKET_ISSUE_DT ")
                .append("AND odtktcpn.SEG_LOCAL_DEP_DT = tktcpn.SEG_DEP_DT AND odtktcpn.SEG_LOCAL_OUT_TM = tktcpn.SEG_DEP_TM ")
                .append("LEFT JOIN  ").append(ticketSchemaName).append(".PNR_FREQ_TRAVLR frqtr ")
                .append("ON tcust.PNR_ORIGNL_ID = frqtr.PNR_LOCTR_ID AND tcust.PNR_ORIGNL_CREATE_DT = frqtr.PNR_CREATE_DT ")
                .append("AND tcust.PARTY_ID = frqtr.PARTY_ID ")
                .append("WHERE ")
                .append("tkt.TICKET_NBR = ? ")
                .append("AND odtkt.OD_SRC_SYS_CD = 'VCR' ")
                .append("AND odtkt.OD_TYPE_CD = 'TRUE_OD' ")
                .append("AND odtkt.OD_LOCAL_DEP_DT = to_date(?, 'YYYY-MM-DD') ")
                .append("AND UPPER(TRIM(tcust.PNR_PAX_FIRST_NM)) LIKE ? ")
                .append("AND UPPER(TRIM(tcust.PNR_PAX_LAST_NM)) = ? ")
                .append("AND odtktcpn.OD_TYPE_CD = 'TRUE_OD' ")
                .append("ORDER BY odtktcpn.SEG_LOCAL_DEP_DT, odtktcpn.SEG_LOCAL_OUT_TM ")
                .toString();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, ticketNumber, departureDate,
                firstName, lastName);
        return ticketReceiptMapper.mapTicketReceipt(sqlRowSet);
    }

    public PassengerDetail findCostDetailsByTicketNumber(SearchCriteria criteria, PassengerDetail passengerDetail) {

        String lastName = criteria.getLastName().toUpperCase().trim();
        String firstName = criteria.getFirstName().toUpperCase().trim() + '%';
        String departureDate = dateFormat.format(criteria.getDepartureDate());
        String ticketNumberSc = criteria.getTicketNumber().trim();
        String ticketNumber10 = (ticketNumberSc.length() == 13) ? ticketNumberSc.substring(3) : ticketNumberSc;
        String ticketNumber13 = (ticketNumberSc.length() == 13) ? ticketNumberSc : new StringBuilder("001").append(ticketNumberSc).toString();

        String commaSpace = "    , ";

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
                .append("    , right(rcptfop.FOP_ACCT_NBR, 4) AS FOP_ACCT_NBR_LAST4 \n")
                .append("    , rcptfop.FOP_CURR_TYPE_CD \n")
                .append("    , rcptfare.FNUM_FARE_AMT \n")
                .append("    , rcptfare.FNUM_FARE_CURR_TYPE_CD \n")
                .append("    , rcptfare.EQFN_FARE_AMT \n")
                .append("    , rcptfare.EQFN_FARE_CURR_TYPE_CD \n")
                .append("    , rcptfare.FARE_TDAM_AMT \n")

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
                .append("    , Right(anclryfop.FOP_ACCT_NBR, 4) AS ANCLRY_FOP_ACCT_NBR_LAST4 \n")
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

                .append("WHERE \n")
                .append("    odtkt.OD_SRC_SYS_CD = 'VCR' \n")
                .append("    AND odtkt.OD_TYPE_CD = 'TRUE_OD' \n")
                .append("    AND odtkt.OD_TICKET_NBR = ? \n")
                .append("    AND rcptfop.DOC_NBR = ? \n")
                .append("    AND odtkt.OD_LOCAL_DEP_DT = to_date(? , 'YYYY-MM-DD') \n")
                .append("    AND UPPER(TRIM(tcust.PNR_PAX_FIRST_NM)) LIKE ? \n")
                .append("    AND UPPER(TRIM(tcust.PNR_PAX_LAST_NM)) = ? \n")
                .append("ORDER BY rcptfop.ISSUE_DT, rcptfop.FOP_SEQ_ID, rcpttax.TAX_CD_SEQ_ID \n")
                .toString();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, ticketNumber10, ticketNumber13, departureDate,
                firstName, lastName);
        if (!sqlRowSet.isBeforeFirst()) {
            throw new ReceiptsMSException("No cost details records found for search criteria = " + criteria);
        }

        return ticketReceiptMapper.mapCostDetails(sqlRowSet, passengerDetail);
    }
}
