package com.aa.fly.receipts.data;

import com.aa.fly.receipts.domain.ReceiptsMSDomainTest;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.TicketReceipt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class AirportRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AirportRepository airportRepository;

    @Test
    public void loadAirportMap() throws ParseException {
        HashMap<String, String> airportMap = new HashMap<>();
        airportMap.put("DFW", "Dallas / Fort Worth airport");
        when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class)))
                .thenReturn(airportMap);
        airportRepository.loadAirportMap();
        String airportName = airportRepository.getAirportName("DFW");
        assertEquals("Dallas / Fort Worth airport", airportName);
    }
}
