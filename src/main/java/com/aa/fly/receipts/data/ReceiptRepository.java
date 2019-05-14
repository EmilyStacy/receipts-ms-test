package com.aa.fly.receipts.data;

import com.aa.fly.receipts.domain.Receipt;
import com.aa.fly.receipts.domain.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 629874 on 5/9/2019.
 */
@Repository
public class ReceiptRepository
{

   @Autowired
   private JdbcTemplate jdbcTemplate;


   @Transactional(readOnly=true)
   public Receipt findReceipt(SearchCriteria criteria) {

      //String paxName = new StringBuilder(criteria.getLastName()).append("/").append(criteria.getFirstName()).toString();

      /*String fareTotal = jdbcTemplate.queryForObject(
            "SELECT  DISTINCT(FARE.FARE_TTL_AMT)\n" + "      FROM    CERT_TCN_VW.TCN_TICKET TKT\n"
                  + "      JOIN     CERT_TCN_VW.TCN_TRAVEL_COUPON CPON\n"
                  + "      ON        TKT.TICKET_NBR = CPON.TICKET_NBR AND TKT.TICKET_ISSUE_DT = CPON.TICKET_ISSUE_DT\n"
                  + "      JOIN     CERT_TCN_VW.TCN_TICKET_FARE_CONSTRC FARE\n"
                  + "      ON        TKT.TICKET_NBR = FARE.TICKET_NBR AND TKT.TICKET_ISSUE_DT = FARE.TICKET_ISSUE_DT\n"
                  + "      WHERE    FARE.FARE_TTL_CURNCY_CD = 'USD'\n"
                  + "      AND        TKT.PNR_PAX_NM = ?\n"
                  + "      AND     CPON.SEG_DEP_DT = To_Timestamp('02/13/2016 00:00:00', 'MM/DD/YYYY HH24:MI:SS') ",
            new Object[]{paxName}, String.class);*/

      String sql = "SELECT    FARE.FARE_TTL_AMT\n" + "FROM    CERT_TCN_VW.TCN_TICKET_FARE_CONSTRC FARE\n"
            + "WHERE    FARE.FARE_TTL_CURNCY_CD = 'USD'\n"
            + "AND        FARE.TICKET_NBR_TXT = " + criteria.getTicketNumber();

      String fareTotal = jdbcTemplate.queryForObject(
            "SELECT    FARE.FARE_TTL_AMT\n" + "FROM    CERT_TCN_VW.TCN_TICKET_FARE_CONSTRC FARE\n"
                  + "WHERE    FARE.FARE_TTL_CURNCY_CD = 'USD'\n"
                  + "AND        FARE.TICKET_NBR_TXT = ?",
            new Object[]{criteria.getTicketNumber()}, String.class);

      return buildReceipt(criteria, fareTotal);
   }

   private Receipt buildReceipt(SearchCriteria criteria, String fareTotal)
   {
      Receipt receipt = new Receipt();
      receipt.setFirstName(criteria.getFirstName());
      receipt.setLastName(criteria.getLastName());
      receipt.setReceiptTotal(fareTotal);
      return receipt;
   }

}
