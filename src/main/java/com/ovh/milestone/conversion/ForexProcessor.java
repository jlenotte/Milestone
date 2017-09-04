package com.ovh.milestone.conversion;

import com.ovh.milestone.Invoice;
import com.ovh.milestone.Main;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.flink.api.common.functions.JoinFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ForexProcessor implements JoinFunction<Invoice, ForexRate, Invoice> {

    private static final Logger LOG = LoggerFactory.getLogger(ForexProcessor.class);



    @Override
    public Invoice join(Invoice first, ForexRate second) throws Exception {

        ZonedDateTime zDate = first.getZonedDate();
        String billDate = zDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Double convertedSum = null;

        // If both dates match, and currency is EUR, do the conversion EUR -> USD
        if (billDate.equals(second.getDate())
            && "EUR".equals(first.getCurrency())) {

            // Log
            LOG.debug(second.getDate(), first.getCurrency());

            // Convert
            convertedSum = Convert.toUsd(second.getForex(), first.getTransaction());

            return new Invoice(
                first.getNichandle(),
                first.getName(),
                first.getFirstName(),
                convertedSum,
                "USD",
                first.getZonedDate());

        } else {
            return new Invoice(
                first.getNichandle(),
                first.getName(),
                first.getFirstName(),
                first.getTransaction(),
                first.getCurrency(),
                first.getZonedDate());
        }
    }
}
