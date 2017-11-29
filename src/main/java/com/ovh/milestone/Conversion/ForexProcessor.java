package com.ovh.milestone.Conversion;

import com.ovh.milestone.Invoice;
import org.apache.flink.api.common.functions.JoinFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ForexProcessor executes the join operation of the convertForex method It checks the dates, and
 * applies the right ForexRate according to the former.
 */

public class ForexProcessor implements JoinFunction<Invoice, ForexRate, Invoice> {


    private static final Logger LOG = LoggerFactory.getLogger(ForexProcessor.class);




    @Override
    public Invoice join(Invoice first, ForexRate second) throws Exception {

        // Log
        // Parse the ZDT to String
        Double convertedSum = null;

        LOG.error(first.getDate() + " / " + second.getDate());

        // If both dates match, and currency is EUR, do the conversion EUR -> USD
        if (first.getDate().equals(second.getDate())
                && "EUR".equals(first.getCurrency())) {

            // Convert
            convertedSum = Convert.toUsd(second.getForex(), first.getTransaction());

            return new Invoice(
                    first.getNichandle(),
                    first.getName(),
                    first.getFirstName(),
                    convertedSum,
                    first.getNewCurrency(),
                    first.getDate());

            // Else return a new invoice with the same values
        }
        else {
            return new Invoice(
                    first.getNichandle(),
                    first.getName(),
                    first.getFirstName(),
                    first.getTransaction(),
                    first.getCurrency(),
                    first.getDate());
        }
    }

}
