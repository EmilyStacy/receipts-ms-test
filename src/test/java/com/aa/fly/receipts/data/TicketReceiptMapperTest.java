package com.aa.fly.receipts.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.fly.receipts.domain.SegmentDetail;
import com.aa.fly.receipts.domain.TicketReceipt;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketReceiptMapperTest {
    @Mock
    private SqlRowSet resultSet;

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private TicketReceiptMapper ticketReceiptMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void mapResultSet() throws SQLException, ParseException {

        Mockito.when(resultSet.next()).thenReturn(true, false); // first time return true and second time return false
        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn("001");
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        final Date departureDate = dateFormat.parse("2019-09-30");
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(departureDate.getTime()));
        ;
        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn("MCO");
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn("MIA");
        Mockito.when(airportRepository.getAirportName("MCO")).thenReturn("Orlando International", "Orlando International");
        Mockito.when(airportRepository.getAirportName("MIA")).thenReturn("Miami International", "Miami International");
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");

        // mock segment details
        Mockito.when(resultSet.getDate("SEG_DEPT_DT")).thenReturn(new java.sql.Date(departureDate.getTime()));
        Mockito.when(resultSet.getString("SEG_DEPT_ARPRT_CD")).thenReturn("MCO");
        Mockito.when(resultSet.getString("SEG_ARVL_ARPRT_CD")).thenReturn("MIA");
        final String segmentDepartureTime = "8:05:00";
        Mockito.when(resultSet.getString("SEG_DEPT_TM")).thenReturn(segmentDepartureTime);
        final String segmentArrivalTime = "9:05:00";
        Mockito.when(resultSet.getString("SEG_ARVL_TM")).thenReturn(segmentArrivalTime);
        Mockito.when(resultSet.getString("FLIGHT_NBR")).thenReturn("1536");
        Mockito.when(resultSet.getString("BOOKING_CLASS")).thenReturn("B");
        Mockito.when(resultSet.getString("FARE_BASE")).thenReturn("QVAJZNB3");
        Mockito.when(resultSet.getString("COUPON_SEQ_NBR")).thenReturn("1");

        TicketReceipt item = ticketReceiptMapper.mapTicketReceipt(resultSet);

        // header
        assertThat(item.getAirlineAccountCode()).isEqualTo("001");
        assertThat(item.getTicketNumber()).isEqualTo("2335038507");
        assertThat(item.getTicketIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(departureDate);
        assertThat(item.getFirstName()).isEqualTo("SIMON");
        assertThat(item.getLastName()).isEqualTo("TEST");
        assertThat(item.getOriginAirportCode()).isEqualTo("MCO");
        assertThat(item.getDestinationAirportCode()).isEqualTo("MIA");
        assertThat(item.getOriginAirport()).isEqualTo("Orlando International");
        assertThat(item.getDestinationAirport()).isEqualTo("Miami International");
        assertThat(item.getPnr()).isEqualTo("MRYMPT");

        // segment details
        assertThat(item.getSegmentDetails().size()).isEqualTo(1);
        SegmentDetail segmentDetail = item.getSegmentDetails().get(0);
        assertThat(segmentDetail.getSegmentDepartureAirportName()).isEqualTo("Orlando International");
        assertThat(segmentDetail.getSegmentArrivalAirportName()).isEqualTo("Miami International");
        assertThat(segmentDetail.getSegmentDepartureDate()).isEqualTo(departureDate);
        assertThat(segmentDetail.getSegmentDepartureAirportCode()).isEqualTo("MCO");
        assertThat(segmentDetail.getSegmentArrivalAirportCode()).isEqualTo("MIA");

        assertThat(segmentDetail.getSegmentDepartureTime()).isEqualTo(segmentDepartureTime);
        assertThat(segmentDetail.getSegmentArrivalTime()).isEqualTo(segmentArrivalTime);
        assertThat(segmentDetail.getFlightNumber()).isEqualTo("1536");
        assertThat(segmentDetail.getCarrierCode()).isEqualTo("AA");
        assertThat(segmentDetail.getBookingClass()).isEqualTo("B");
        assertThat(segmentDetail.getFareBasis()).isEqualTo("QVAJZNB3");
        assertThat(segmentDetail.getCouponSeqNumber()).isEqualTo("1");

    }

    @Test
    public void mapResultSetWithNulls() throws SQLException, ParseException {

        Mockito.when(resultSet.next()).thenReturn(true, false); // first time return true and second time return false
        Mockito.when(resultSet.getString("AIRLN_ACCT_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("TICKET_NBR")).thenReturn("2335038507");
        Mockito.when(resultSet.getDate("TICKET_ISSUE_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-03-14").getTime()));
        Mockito.when(resultSet.getDate("DEP_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-09-30").getTime()));
        ;
        Mockito.when(resultSet.getString("FIRST_NM")).thenReturn("SIMON");
        Mockito.when(resultSet.getString("LAST_NM")).thenReturn("TEST");
        Mockito.when(resultSet.getString("ORG_ATO_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("DEST_ATO_CD")).thenReturn(null);
        Mockito.when(airportRepository.getAirportName(null)).thenReturn(null, null, null, null);
        Mockito.when(resultSet.getString("PNR")).thenReturn("MRYMPT");

        TicketReceipt item = ticketReceiptMapper.mapTicketReceipt(resultSet);
        assertThat(item.getAirlineAccountCode()).isNull();
        assertThat(item.getTicketNumber()).isEqualTo("2335038507");
        assertThat(item.getTicketIssueDate()).isEqualTo(dateFormat.parse("2019-03-14"));
        assertThat(item.getDepartureDate()).isEqualTo(dateFormat.parse("2019-09-30"));
        assertThat(item.getFirstName()).isEqualTo("SIMON");
        assertThat(item.getLastName()).isEqualTo("TEST");
        assertThat(item.getOriginAirportCode()).isNull();
        assertThat(item.getDestinationAirportCode()).isNull();
        assertThat(item.getPnr()).isEqualTo("MRYMPT");
    }
}
