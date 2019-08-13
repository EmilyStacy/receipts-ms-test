package com.aa.fly.receipts.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.aa.fly.receipts.domain.TicketSummary;

public class TicketSummaryMapper implements RowMapper<TicketSummary> {

    @Override
    public TicketSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        TicketSummary ticketSummary = new TicketSummary();
        
        ticketSummary.setAirlineAccountCode(rs.getString("AIRLN_ACCT_CD") != null ? rs.getString("AIRLN_ACCT_CD").trim() : null);
        ticketSummary.setTicketNumber(rs.getString("TICKET_NBR"));
        ticketSummary.setTicketIssueDate(rs.getDate("TICKET_ISSUE_DT"));
        ticketSummary.setDepartureDate(rs.getDate("DEP_DT"));
        ticketSummary.setFirstName(rs.getString("FIRST_NM"));
        ticketSummary.setLastName(rs.getString("LAST_NM"));
        ticketSummary.setOriginAirportCode(rs.getString("ORG_ATO_CD") != null ? rs.getString("ORG_ATO_CD").trim() : null);
        ticketSummary.setDestinationAirportCode(rs.getString("DEST_ATO_CD") != null ? rs.getString("DEST_ATO_CD").trim() : null);
        ticketSummary.setOriginAirport(rs.getString("ORG_ATO_NM"));
        ticketSummary.setDestinationAirport(rs.getString("DEST_ATO_NM"));
        ticketSummary.setPnr(rs.getString("PNR"));

        return ticketSummary;
    }

}
