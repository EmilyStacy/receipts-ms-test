package com.aa.fly.receipts.data;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;

@Repository
public class TicketReceiptRepository {

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
                // =================== trip details =======================
                .append("    odtktcpn.SEG_LOCAL_DEP_DT AS SEG_DEPT_DT, ")
                .append("    odtktcpn.SEG_DEP_AIRPRT_IATA_CD AS SEG_DEPT_ARPRT_CD, ")
                .append("    odtktcpn.SEG_ARVL_AIRPRT_IATA_CD AS SEG_ARVL_ARPRT_CD, ")
                .append("    odtktcpn.SEG_LOCAL_OUT_TM AS SEG_DEPT_TM, ")
                .append("    odtktcpn.SEG_LOCAL_IN_TM AS SEG_ARVL_TM, ")
                .append("    tktcpn.FLOWN_OPERAT_FLIGHT_NBR AS FLIGHT_NBR, ")
                .append("    tktcpn.ACCT_FARE_CLASS_CD AS BOOKING_CLASS, ")
                .append("    tktcpn.FARE_BASE_CD AS FARE_BASE, ")
                .append("    odtktcpn.OD_TICKET_COUPON_SEQ_NBR AS COUPON_SEQ_NBR ")
                .append("FROM ").append(ticketSchemaName).append(".OD_TICKET odtkt ")
                .append("JOIN  ").append(ticketSchemaName).append(".TICKET_CUSTOMER tcust ")
                .append("ON odtkt.OD_TICKET_NBR = tcust.TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = tcust.TICKET_ISSUE_DT ")
                .append("JOIN  ").append(ticketSchemaName).append(".TICKET tkt ")
                .append("ON odtkt.OD_TICKET_NBR = tkt.TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = tkt.TICKET_ISSUE_DT ")
                .append("JOIN  ").append(ticketSchemaName).append(".OD_TICKET_TRAVEL_COUPON odtktcpn ")
                .append("ON odtkt.OD_TICKET_NBR = odtktcpn.OD_TICKET_NBR AND odtkt.OD_TICKET_ISSUE_DT = odtktcpn.OD_TICKET_ISSUE_DT ")
                .append("JOIN  ").append(ticketSchemaName).append(".TICKET_COUPON tktcpn ")
                .append("ON odtktcpn.OD_TICKET_NBR = tktcpn.TICKET_NBR AND odtktcpn.OD_TICKET_ISSUE_DT = tktcpn.TICKET_ISSUE_DT ")
                .append("AND odtktcpn.SEG_LOCAL_DEP_DT = tktcpn.SEG_DEP_DT AND odtktcpn.SEG_LOCAL_OUT_TM = tktcpn.SEG_DEP_TM ")
                .append("WHERE ")
                .append("odtkt.OD_TICKET_NBR = ? ")
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

}
