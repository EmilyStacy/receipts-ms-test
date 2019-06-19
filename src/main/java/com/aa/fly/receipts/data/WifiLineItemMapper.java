package com.aa.fly.receipts.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.aa.fly.receipts.domain.WifiLineItem;

public class WifiLineItemMapper implements RowMapper<WifiLineItem> {

    @Override
    public WifiLineItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        WifiLineItem lineItem = new WifiLineItem();
        lineItem.setCcLastFour(rs.getString("LAST_4_DIGIT") != null ? rs.getString("LAST_4_DIGIT").trim() : null);
        lineItem.setCcTypeCode(rs.getString("CC_TYPE_CD") != null ? rs.getString("CC_TYPE_CD").trim() : null);
        lineItem.setCurrencyCode(rs.getString("CURNCY_CD") != null ? rs.getString("CURNCY_CD").trim() : null);
        lineItem.setLastName(
                rs.getString("CARD_HLDR_LAST_NM") != null ? rs.getString("CARD_HLDR_LAST_NM").trim() : null);
        lineItem.setNetPrice(rs.getString("NET_PRICE"));
        lineItem.setOrderId(rs.getString("SUBSCR_ORDER_ID"));
        lineItem.setProductName(rs.getString("PROD_NM") != null ? rs.getString("PROD_NM").trim() : null);
        lineItem.setProductPrice(rs.getString("PROD_PRICE"));
        lineItem.setPurchaseDate(rs.getDate("PURCHS_DT"));
        lineItem.setTaxAmount(rs.getString("TAX_AMT"));
        return lineItem;
    }

}
