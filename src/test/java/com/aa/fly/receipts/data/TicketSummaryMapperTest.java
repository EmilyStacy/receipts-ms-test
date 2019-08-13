package com.aa.fly.receipts.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.TicketSummary;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketSummaryMapperTest {
    @Mock
    private ResultSet resultSet;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void mapResultSet() throws SQLException, ParseException {

        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn("001");
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-09-30").getTime()));
        ;
        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn("MCO");
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn("MIA");
        Mockito.when(resultSet.getString("ORG_ATO_NM")).thenReturn("Orlando International");
        Mockito.when(resultSet.getString("DEST_ATO_NM")).thenReturn("Miami International");
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");

        TicketSummaryMapper mapper = new TicketSummaryMapper();
        TicketSummary item = mapper.mapRow(resultSet, 0);
        assertThat(item.getAirlineAccountCode()).isEqualTo("001");
        assertThat(item.getTicketNumber()).isEqualTo("2335038507");
        assertThat(item.getTicketIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(dateFormat.parse("2019-09-30"));
        assertThat(item.getFirstName()).isEqualTo("SIMON");
        assertThat(item.getLastName()).isEqualTo("TEST");
        assertThat(item.getOriginAirportCode()).isEqualTo("MCO");
        assertThat(item.getDestinationAirportCode()).isEqualTo("MIA");
        assertThat(item.getOriginAirport()).isEqualTo("Orlando International");
        assertThat(item.getDestinationAirport()).isEqualTo("Miami International");
        assertThat(item.getPnr()).isEqualTo("MRYMPT");
    }
    
    @Test
    public void mapResultSetWithNulls() throws SQLException, ParseException {

        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-09-30").getTime()));
        ;
        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("ORG_ATO_NM")).thenReturn("Orlando International");
        Mockito.when(resultSet.getString("DEST_ATO_NM")).thenReturn("Miami International");
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");

        TicketSummaryMapper mapper = new TicketSummaryMapper();
        TicketSummary item = mapper.mapRow(resultSet, 0);
        assertThat(item.getAirlineAccountCode()).isNull();
        assertThat(item.getTicketNumber()).isEqualTo("2335038507");
        assertThat(item.getTicketIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(dateFormat.parse("2019-09-30"));
        assertThat(item.getFirstName()).isEqualTo("SIMON");
        assertThat(item.getLastName()).isEqualTo("TEST");
        assertThat(item.getOriginAirportCode()).isNull();
        assertThat(item.getDestinationAirportCode()).isNull();
        assertThat(item.getOriginAirport()).isEqualTo("Orlando International");
        assertThat(item.getDestinationAirport()).isEqualTo("Miami International");
        assertThat(item.getPnr()).isEqualTo("MRYMPT");
    }
}
