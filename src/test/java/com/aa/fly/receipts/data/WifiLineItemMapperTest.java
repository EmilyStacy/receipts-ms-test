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

import com.aa.fly.receipts.domain.WifiLineItem;

/**
 * Created by 629874 on 5/17/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class WifiLineItemMapperTest {
    @Mock
    private ResultSet resultSet;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void mapResultSet() throws SQLException, ParseException {
        Mockito.when(resultSet.getString("CREDIT_CARD_LAST_4_NBR")).thenReturn("1234");
        Mockito.when(resultSet.getString("CC_TYPE_CD")).thenReturn("MC");
        Mockito.when(resultSet.getString("CURNCY_CD")).thenReturn("USD");
        Mockito.when(resultSet.getString("CARD_HLDR_LAST_NM")).thenReturn("LAST");
        Mockito.when(resultSet.getString("NET_PRICE")).thenReturn("11.00");
        Mockito.when(resultSet.getString("SUBSCR_ORDER_ID")).thenReturn("123");
        Mockito.when(resultSet.getString("PROD_NM")).thenReturn("WIFI SUBSCRIPTION");
        Mockito.when(resultSet.getString("PROD_PRICE")).thenReturn("10.00");
        Mockito.when(resultSet.getDate("PURCHS_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-01-01").getTime()));
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("1.00");
        WifiLineItemMapper mapper = new WifiLineItemMapper();
        WifiLineItem item = mapper.mapRow(resultSet, 0);
        assertThat(item.getCcLastFour()).isEqualTo("1234");
        assertThat(item.getCcTypeCode()).isEqualTo("MC");
        assertThat(item.getCurrencyCode()).isEqualTo("USD");
        assertThat(item.getLastName()).isEqualTo("LAST");
        assertThat(item.getNetPrice()).isEqualTo("11.00");
        assertThat(item.getOrderId()).isEqualTo("123");
        assertThat(item.getProductName()).isEqualTo("WIFI SUBSCRIPTION");
        assertThat(item.getProductPrice()).isEqualTo("10.00");
        assertThat(item.getPurchaseDate()).isEqualTo(dateFormat.parse("2019-01-01"));
        assertThat(item.getTaxAmount()).isEqualTo("1.00");
        assertThat(item.getSeller()).isEqualTo("American");
    }

    @Test
    public void mapResultSetWithOptionalNulls() throws SQLException, ParseException {
        Mockito.when(resultSet.getString("CREDIT_CARD_LAST_4_NBR")).thenReturn(null);
        Mockito.when(resultSet.getString("CC_TYPE_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("CURNCY_CD")).thenReturn(null);
        Mockito.when(resultSet.getString("CARD_HLDR_LAST_NM")).thenReturn(null);
        Mockito.when(resultSet.getString("NET_PRICE")).thenReturn("11.00");
        Mockito.when(resultSet.getString("SUBSCR_ORDER_ID")).thenReturn("123");
        Mockito.when(resultSet.getString("PROD_NM")).thenReturn(null);
        Mockito.when(resultSet.getString("PROD_PRICE")).thenReturn("10.00");
        Mockito.when(resultSet.getDate("PURCHS_DT")).thenReturn(new java.sql.Date(dateFormat.parse("2019-01-01").getTime()));
        Mockito.when(resultSet.getString("TAX_AMT")).thenReturn("1.00");
        WifiLineItemMapper mapper = new WifiLineItemMapper();
        WifiLineItem item = mapper.mapRow(resultSet, 0);
        assertThat(item.getCcLastFour()).isNull();
        assertThat(item.getCcTypeCode()).isNull();
        assertThat(item.getCurrencyCode()).isNull();
        assertThat(item.getLastName()).isNull();
        assertThat(item.getNetPrice()).isEqualTo("11.00");
        assertThat(item.getOrderId()).isEqualTo("123");
        assertThat(item.getProductName()).isNull();
        assertThat(item.getProductPrice()).isEqualTo("10.00");
        assertThat(item.getPurchaseDate()).isEqualTo(dateFormat.parse("2019-01-01"));
        assertThat(item.getTaxAmount()).isEqualTo("1.00");
        assertThat(item.getSeller()).isEqualTo("American");
    }
}
