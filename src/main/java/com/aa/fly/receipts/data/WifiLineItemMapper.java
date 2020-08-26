package com.aa.fly.receipts.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.aa.fly.receipts.domain.WifiLineItem;

public class WifiLineItemMapper implements RowMapper<WifiLineItem> {

    @Override
    public WifiLineItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        WifiLineItem lineItem = new WifiLineItem();
        lineItem.setCcLastFour(rs.getString("CREDIT_CARD_LAST_4_NBR") != null ? rs.getString("CREDIT_CARD_LAST_4_NBR").trim() : null);
        lineItem.setCcTypeCode(rs.getString("CREDIT_CARD_TYPE") != null ? rs.getString("CREDIT_CARD_TYPE").trim() : null);
        lineItem.setCcTypeName(rs.getString("CC_TYPE_NM") != null ? rs.getString("CC_TYPE_NM").trim() : null);
        lineItem.setCurrencyCode(rs.getString("CURNCY_CD") != null ? rs.getString("CURNCY_CD").trim() : null);
        lineItem.setLastName(
                rs.getString("CARD_HLDR_LAST_NM") != null ? rs.getString("CARD_HLDR_LAST_NM").trim() : null);
        lineItem.setNetPrice(rs.getString("NET_PRICE"));
        lineItem.setOrderId(rs.getString("SUBSCR_ORDER_LINE_UID"));
        lineItem.setProductName(rs.getString("PROD_NM") != null ? rs.getString("PROD_NM").trim() : null);
        lineItem.setProductPrice(rs.getString("PROD_PRICE"));
        lineItem.setPurchaseDate(rs.getDate("PURCHS_DT"));
        lineItem.setTaxAmount(rs.getString("TAX_AMT"));
        lineItem.setSeller("American");

        return lineItem;
    }

}
