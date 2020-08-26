package com.aa.fly.receipts.data;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aa.fly.receipts.domain.WifiLineItem;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;

/**
 * Created by 629874 on 5/9/2019.
 */
@Repository
public class WifiReceiptRepository {

    @Autowired
    @Qualifier("jdbcTemplateWifi")
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.inflight.schema.name:CERT_INFLIGHT_SRVCS_VW}")
    private String inflightSchemaName;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Transactional(readOnly = true)
    public WifiReceipt findWifiReceipt(WifiSearchCriteria criteria) {
        String lastName = criteria.getLastName().toUpperCase();
        // hard code the time part of the start date to zeroes
        String fromDate = dateFormat.format(criteria.getStartDate()) + " 00:00:00";
        // hard code the time part of the end date
        String endDate = dateFormat.format(criteria.getEndDate()) + " 23:59:59";
        String ccLastFour = criteria.getCcLastFour();

        // New sql joins to RiM Auth to get last 4 CC
        String sql = new StringBuilder("SELECT ")
                //.append("    gogo.SUBSCR_ORDER_ID, ")
                .append("    gogo.SUBSCR_ORDER_LINE_UID, ")
                .append("    CAST(gogo.PURCHS_TMS AS DATE) PURCHS_DT, ")
                .append("    gogo.PROD_NM, ")
                .append("    gogo.PROD_PRICE, ")
                .append("    gogo.CURNCY_CD, ")
                .append("    gogo.TAX_AMT, ")
                .append("    gogo.NET_PRICE, ")
                .append("    rim.CREDIT_CARD_LAST_4_NBR, ")
                .append("    rim.CREDIT_CARD_TYPE, ")
                .append("    Coalesce(cctype.CREDIT_CARD_TYPE_ALIAS_DESC, 'CARD') CC_TYPE_NM, ")
                .append("    rim.CARD_HLDR_LAST_NM ")
                .append("FROM ").append(inflightSchemaName).append(".WIFI_SUBSCR_PURCHS gogo ")
                .append("JOIN ").append(inflightSchemaName).append(".RIM_AUTHRZ_EVENT rim ")
                .append("ON gogo.GATE_WAY_REFRNC_ID = rim.GATE_WAY_REFRNC_ID ")
                .append("LEFT JOIN ").append(inflightSchemaName).append(".CREDIT_CARD_TYPE_ALIAS cctype ")
                .append("ON rim.CREDIT_CARD_TYPE = cctype.CREDIT_CARD_TYPE_ALIAS_CD ")
                .append("WHERE ")
                .append("UPPER(rim.CARD_HLDR_LAST_NM) = ? ")
                .append("AND gogo.PURCHS_TMS BETWEEN To_Timestamp(?, 'MM/DD/YYYY HH24:MI:SS') AND To_Timestamp(?, 'MM/DD/YYYY HH24:MI:SS') ")
                .append("AND rim.CREDIT_CARD_LAST_4_NBR = ? ")
                .append("AND rim.AUTHRZ_APRVL_IND = 'Y' ")
                .append("AND gogo.INFO_SRC_TYPE_CD = 'ASP' ")
                .append("ORDER BY PURCHS_DT").toString();
        
        List<WifiLineItem> wifiLineItems = jdbcTemplate.query(sql, new WifiLineItemMapper(), lastName, fromDate,
                endDate, ccLastFour);
        WifiReceipt wifiReceipt = new WifiReceipt();
        wifiReceipt.setWifiLineItems(wifiLineItems);
        return wifiReceipt;
    }

}
