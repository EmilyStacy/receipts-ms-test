package com.aa.fly.receipts.data;

import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class TicketReceiptRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional(readOnly = true)
    public TicketSummary findTicketSummaryByTicketNumber(SearchCriteria criteria) {
        String lastName = criteria.getLastName().toUpperCase().trim();
        String firstName = criteria.getFirstName().toUpperCase().trim() + '%';
        String departureDate = dateFormat.format(criteria.getDepartureDate());
        String ticketNumber = criteria.getTicketNumber().trim().substring(3);

        String sql = new StringBuilder("SELECT ")
                .append("    odt.OD_TICKET_AIRLN_ACCT_CD AS AIRLN_ACCT_CD, ")
                .append("    odt.OD_TICKET_NBR AS TICKET_NBR, ")
                .append("    odt.OD_TICKET_ISSUE_DT AS TICKET_ISSUE_DT, ")
                .append("    odt.OD_LOCAL_DEP_DT AS DEP_DT, ")
                .append("    tcu.PNR_PAX_FIRST_NM AS FIRST_NM, ")
                .append("    tcu.PNR_PAX_LAST_NM AS LAST_NM, ")
                .append("    odt.OD_ORIGIN_AIRPRT_IATA_CD AS ORG_ATO_CD, ")
                .append("    odt.OD_DESTNTN_AIRPRT_IATA_CD AS DEST_ATO_CD, ")
                .append("    air1.AIRPRT_NM AS ORG_ATO_NM, ")
                .append("    air2.AIRPRT_NM AS DEST_ATO_NM, ")
                .append("    tkt.PNR_LOCTR_ID AS PNR ")
                .append("FROM ").append(ticketSchemaName).append(".OD_TICKET odt ")
                .append("JOIN  ").append(ticketSchemaName).append(".TICKET_CUSTOMER tcu ")
                .append("ON odt.OD_TICKET_NBR = tcu.TICKET_NBR AND odt.OD_TICKET_ISSUE_DT = tcu.TICKET_ISSUE_DT ")
                .append("JOIN  ").append(ticketSchemaName).append(".TICKET tkt ")
                .append("ON odt.OD_TICKET_NBR = tkt.TICKET_NBR AND odt.OD_TICKET_ISSUE_DT = tkt.TICKET_ISSUE_DT ")
                .append("JOIN  ").append(ticketSchemaName).append(".AIRPORT_STATION_CURRENT air1 ")
                .append("ON odt.OD_ORIGIN_AIRPRT_IATA_CD = air1.AIRPRT_CD ")
                .append("JOIN  ").append(ticketSchemaName).append(".AIRPORT_STATION_CURRENT air2 ")
                .append("ON odt.OD_DESTNTN_AIRPRT_IATA_CD = air2.AIRPRT_CD ")
                .append("WHERE ")
                .append("odt.OD_TICKET_NBR = ? ")
                .append("AND odt.OD_SRC_SYS_CD = 'VCR' ")
                .append("AND odt.OD_TYPE_CD = 'FAREBASIS_OD' ")
                .append("AND odt.OD_LOCAL_DEP_DT = to_date(?, 'YYYY-MM-DD') ")
                .append("AND UPPER(TRIM(tcu.PNR_PAX_FIRST_NM)) LIKE ? ")
                .append("AND UPPER(TRIM(tcu.PNR_PAX_LAST_NM)) = ? ").toString();

        List<TicketSummary> ticketSummaryList = jdbcTemplate.query(sql, new TicketSummaryMapper(), ticketNumber, departureDate,
                firstName, lastName);
        return CollectionUtils.isEmpty(ticketSummaryList) ? null : ticketSummaryList.get(0);
    }

}
