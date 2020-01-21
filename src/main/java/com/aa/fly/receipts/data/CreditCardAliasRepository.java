package com.aa.fly.receipts.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CreditCardAliasRepository {

    private static final Logger logger = LoggerFactory.getLogger(CreditCardAliasRepository.class);

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Map<String, String> creditCardAliasMap = new HashMap<>();

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 2 * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void loadCreditCardAliases() {
        logger.info("Loading credit card names........");
        String sql = new StringBuilder("\nSELECT \n\t")
                .append("ccalias.CREDIT_CARD_TYPE_ALIAS_CD \n\t")
                .append(",ccalias.CREDIT_CARD_TYPE_ALIAS_DESC \n")
                .append("FROM \n\t")
                .append(ticketSchemaName)
                .append(".CREDIT_CARD_TYPE_ALIAS ccalias \n")
                .append("WHERE \n\t")
                .append("ccalias.CREDIT_CARD_TYPE_ALIAS_DESC <> 'UNKNOWN'\n").toString();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        creditCardAliasMap.clear();
        while (sqlRowSet.next()) {
            String alias = sqlRowSet.getString("CREDIT_CARD_TYPE_ALIAS_CD").trim();
            if (alias != null && alias.length() > 0) {
                alias = "CC" + alias;
            }
            String description = StringUtils.isNotBlank(sqlRowSet.getString("CREDIT_CARD_TYPE_ALIAS_DESC")) ? sqlRowSet.getString("CREDIT_CARD_TYPE_ALIAS_DESC") : "UNKNOWN" ;
            creditCardAliasMap.put(alias, toTitleCase(description));
        }

        if (logger.isInfoEnabled()) {
            logger.info("Loaded {} credit card names from Mosaic", creditCardAliasMap.size());
        }
    }

    public Map<String, String> getCreditCardAliasMap() {
        return creditCardAliasMap;
    }

    private String toTitleCase(String input) {
        if (input == null)
            return null;
        StringBuilder titleCase = new StringBuilder();
        String[] tokens = input.split(" ");
        int i = 0;
        for (String token : tokens) {
            if (token.length() > 3) {
                token = token.toLowerCase();
            }
            titleCase.append(StringUtils.capitalize(token));
            if (i++ < tokens.length) {
                titleCase.append(" ");
            }
        }
        return titleCase.toString().trim();
    }
}
