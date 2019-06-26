package com.aa.fly.receipts.data;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import com.aa.fly.receipts.domain.WifiLineItem;
import com.aa.fly.receipts.domain.WifiReceipt;
import com.aa.fly.receipts.domain.WifiSearchCriteria;

/**
 * Created by 629874 on 5/9/2019.
 */
@Repository
public class ReceiptRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mosaic.inflight.schema.name:CERT_INFLIGHT_SRVCS_VW}")
    private String inflightSchemaName;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Transactional(readOnly = true)
    public Receipt findReceipt(SearchCriteria criteria) {

        String fareTotal = jdbcTemplate.queryForObject(
                "SELECT    FARE.FARE_TTL_AMT\n" + "FROM    CERT_TCN_VW.TCN_TICKET_FARE_CONSTRC FARE\n"
                        + "WHERE    FARE.FARE_TTL_CURNCY_CD = 'USD'\n" + "AND        FARE.TICKET_NBR_TXT = ?",
                new Object[] {
                        criteria.getTicketNumber()
                }, String.class);

        return buildReceipt(criteria, fareTotal);
    }

    @Transactional(readOnly = true)
    public WifiReceipt findWifiReceipt(WifiSearchCriteria criteria) {
        // build query params
        String lastName = criteria.getLastName().toUpperCase();
        // hard code the time part of the start date to zeroes
        String fromDate = dateFormat.format(criteria.getStartDate()) + " 00:00:00";
        // hard code the time part of the end date
        String endDate = dateFormat.format(criteria.getEndDate()) + " 23:59:59";
        String ccLastFour = criteria.getCcLastFour();

        // build the query for Mosaic
        /*
         * String sql = "SELECT  SUBSCR_ORDER_ID, CAST(PURCHS_TMS AS DATE) PURCHS_DT, PROD_NM, PROD_PRICE, \n" + "CURNCY_CD, TAX_AMT, NET_PRICE, LAST_4_DIGIT, CC_TYPE_CD, CARD_HLDR_LAST_NM FROM " +
         * inflightSchemaName + ".WIFI_SUBSCR_PURCHS \n" + " WHERE UPPER(CARD_HLDR_LAST_NM) = ? AND INFO_SRC_TYPE_CD = 'ASP' AND PURCHS_TMS\n" +
         * " BETWEEN To_Timestamp(?, 'MM/DD/YYYY HH24:MI:SS') AND To_Timestamp(?, 'MM/DD/YYYY HH24:MI:SS') ORDER BY PURCHS_DT";
         */

        // New sql joins to RiM Auth to get last 4 CC
        String sql = "SELECT "
                + "    wifi.SUBSCR_ORDER_ID, "
                + "    CAST(wifi.PURCHS_TMS AS DATE) PURCHS_DT, "
                + "    wifi.PROD_NM, "
                + "    wifi.PROD_PRICE, "
                + "    wifi.CURNCY_CD, "
                + "    wifi.TAX_AMT, "
                + "    wifi.NET_PRICE, "
                + "    rim.CREDIT_CARD_LAST_4_NBR, "
                + "    wifi.CC_TYPE_CD, "
                + "    wifi.CARD_HLDR_LAST_NM "
                + "FROM " + inflightSchemaName + ".WIFI_SUBSCR_PURCHS wifi "
                + "JOIN " + inflightSchemaName + ".RIM_AUTHRZ_EVENT rim "
                + "ON wifi.GATE_WAY_REFRNC_ID = rim.GATE_WAY_REFRNC_ID "
                + "WHERE "
                + "wifi.INFO_SRC_TYPE_CD = 'ASP' "
                + "AND UPPER(wifi.CARD_HLDR_LAST_NM) = ? "
                + "AND wifi.PURCHS_TMS BETWEEN To_Timestamp(?, 'MM/DD/YYYY HH24:MI:SS') AND To_Timestamp(?, 'MM/DD/YYYY HH24:MI:SS') "
                + "AND rim.CREDIT_CARD_LAST_4_NBR = ? "
                + "ORDER BY PURCHS_DT";

        List<WifiLineItem> wifiLineItems = jdbcTemplate.query(sql, new WifiLineItemMapper(), lastName, fromDate,
                endDate, ccLastFour);
        WifiReceipt wifiReceipt = new WifiReceipt();
        wifiReceipt.setWifiLineItems(wifiLineItems);
        return wifiReceipt;
    }

    private Receipt buildReceipt(SearchCriteria criteria, String fareTotal) {
        Receipt receipt = new Receipt();
        receipt.setFirstName(criteria.getFirstName());
        receipt.setLastName(criteria.getLastName());
        receipt.setReceiptTotal(fareTotal);
        return receipt;
    }

}
