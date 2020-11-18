package com.aa.fly.receipts.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import java.sql.Date;

import com.aa.fly.receipts.domain.TaxCodeAndDescription;

@RunWith(SpringJUnit4ClassRunner.class)
public class TaxDescriptionRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SqlRowSet resultSet;

    @InjectMocks
    private TaxDescriptionRepository taxDescriptionRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Test
    public void testGetDescription_uniqueDescription_ticketIssueDateIsEffectiveStartDate() throws ParseException {
        ReflectionTestUtils.setField(taxDescriptionRepository, "taxCodeAndDescriptionMap", getTaxCodeDescriptionMap());
        String description = taxDescriptionRepository.getDescription("CB", dateFormat.parse("03/13/2009"));
        assertThat(description).isEqualTo("PASSENGER SVC CHARGE");
    }

    @Test
    public void testGetDescription_multipleDescriptions_ticketIssueDateIsEffectiveStartDateOfPastDateRange() throws ParseException {
        ReflectionTestUtils.setField(taxDescriptionRepository, "taxCodeAndDescriptionMap", getTaxCodeDescriptionMap());
        String description = taxDescriptionRepository.getDescription("CS", dateFormat.parse("03/31/2014"));
        assertThat(description).isEqualTo("CONSUMPTION TAX");
    }

    @Test
    public void testGetDescription_multipleDescriptions_ticketIssueDateIsEffectiveStartDateOfCurrentDateRange() throws ParseException {
        ReflectionTestUtils.setField(taxDescriptionRepository, "taxCodeAndDescriptionMap", getTaxCodeDescriptionMap());
        String description = taxDescriptionRepository.getDescription("CS", dateFormat.parse("07/13/2018"));
        assertThat(description).isEqualTo("AVIATION SECURITY FEE");
    }


    private Map<String, List<TaxCodeAndDescription>> getTaxCodeDescriptionMap() throws ParseException {
        Map<String, List<TaxCodeAndDescription>> taxCodeAndDescriptionMap = new HashMap<>();
        taxCodeAndDescriptionMap.put("CB", Arrays.asList(new TaxCodeAndDescription("CB", "PASSENGER SVC CHARGE", dateFormat.parse("03/13/2009"), dateFormat.parse("12/31/9999"))));
        taxCodeAndDescriptionMap.put("CS", Arrays.asList(new TaxCodeAndDescription("CS", "CONSUMPTION TAX", dateFormat.parse("03/31/2014"), dateFormat.parse("07/12/2018")), new TaxCodeAndDescription("CS", "AVIATION SECURITY FEE", dateFormat.parse("07/13/2018"), dateFormat.parse("12/31/9999"))));
        return taxCodeAndDescriptionMap;
    }

    @Test
    public void testTaxCodeAndDescriptionMap() {
        Mockito.when(jdbcTemplate.queryForRowSet(anyString()))
                .thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString("TAX_CD")).thenReturn("AnyRandomValue");
        Mockito.when(resultSet.getString("TAX_CD_DESC")).thenReturn("AnyRandomValue");
        Mockito.when(resultSet.getDate("EFF_START_DT")).thenReturn(Date.valueOf("2020-01-26"));
        Mockito.when(resultSet.getDate("EFF_END_DT")).thenReturn(Date.valueOf("2020-02-26"));
        taxDescriptionRepository.loadTaxDescriptions();
        Assert.assertEquals(1, taxDescriptionRepository.getTaxCodeAndDescriptionMap().size());
    }
}
