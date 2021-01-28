package com.aa.fly.receipts.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class TicketViewRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    public SqlRowSet findTicketViewByTicketNumber(String ticketNumber) {

        String sql = new StringBuilder("\nSELECT ")
        .append("    TICKET_NBR, \n")
        .append("    TICKET_ISSUE_DT \n")
        .append("FROM ").append(ticketSchemaName).append(".TICKET \n")
        .append("WHERE \n")
        .append("TICKET_NBR = ? \n")
        .append("ORDER BY TICKET_ISSUE_DT DESC \n")
        .toString();
        
        return jdbcTemplate.queryForRowSet(sql, ticketNumber);
    }
}
