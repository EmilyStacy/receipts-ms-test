package com.aa.fly.receipts.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.aa.fly.receipts.domain.TaxCodeAndDescription;

@Repository
public class TaxDescriptionRepository {
    private static final Logger logger = LoggerFactory.getLogger(TaxDescriptionRepository.class);

    @Value("${mosaic.ticket.schema.name:CERT_TCN_RECPT_VW}")
    private String ticketSchemaName;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Map<String, List<TaxCodeAndDescription>> taxCodeAndDescriptionMap = new HashMap<>();

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 2 * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void loadTaxDescriptions() {
        logger.info("Loading credit card names........");
        String sql = new StringBuilder("\nSELECT \n\t")
                .append("TAX_CD \n\t")
                .append(",TAX_CD_DESC \n")
                .append(",EFF_START_DT \n")
                .append(",EFF_END_DT \n")
                .append("FROM \n\t")
                .append(ticketSchemaName)
                .append(".TCN_TAX_CD \n").toString();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        taxCodeAndDescriptionMap.clear();

        while (sqlRowSet.next()) {
            String taxCode = sqlRowSet.getString("TAX_CD").trim();
            String taxDescription = sqlRowSet.getString("TAX_CD_DESC").trim();
            Date startDate = sqlRowSet.getDate("EFF_START_DT");
            Date endDate = sqlRowSet.getDate("EFF_END_DT");
            List<TaxCodeAndDescription> taxCodeAndDescriptions = taxCodeAndDescriptionMap.get(taxCode);
            if(taxCodeAndDescriptions == null) {
                taxCodeAndDescriptions = new ArrayList<>();
            }
            taxCodeAndDescriptions.add(new TaxCodeAndDescription(taxCode, taxDescription, startDate, endDate));
            taxCodeAndDescriptionMap.put(taxCode, taxCodeAndDescriptions);
        }

        if (logger.isInfoEnabled()) {
            logger.info("Loaded {} tax codes and descriptions from Mosaic", taxCodeAndDescriptionMap
                    .entrySet().size());
            logger.info("{}",taxCodeAndDescriptionMap.values());
        }
    }

    public String getDescription(String taxCode, Date ticketIssueDate) {
        List<TaxCodeAndDescription> taxCodeAndDescriptions = taxCodeAndDescriptionMap.get(taxCode);

        String description = taxCode;
        if(taxCodeAndDescriptions!= null) {
            if(taxCodeAndDescriptions.size() == 1) {
                description = taxCodeAndDescriptions.get(0).getTaxCodeDescription();
            } else if(taxCodeAndDescriptions.size() > 1) {
                for(TaxCodeAndDescription taxCodeAndDescription : taxCodeAndDescriptions) {
                    boolean isIssueDateBetweenStartAndEndDate = !(ticketIssueDate.before(taxCodeAndDescription.getStartDate()) || ticketIssueDate.after(taxCodeAndDescription.getEndDate()));

                    if(isIssueDateBetweenStartAndEndDate) {
                        description = taxCodeAndDescription.getTaxCodeDescription();
                    }
                }
            }
        }

        return description;
    }
}
