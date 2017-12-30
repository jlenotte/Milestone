package com.ovh.milestone.FlinkJobs;

import com.ovh.milestone.Invoice;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Calculate the total revenue for each nichandle
 */


public class PerNicTotal {


    private static final Logger LOG = LoggerFactory.getLogger(PerNicTotal.class);




    /**
     * Get the sum of all transactions per nic
     *
     * @param data DataSet
     * @return MapOperator
     */
    public MapOperator<Invoice, Tuple2<String, Double>> getNichandleSumFlink(
            DataSet<Invoice> data) {
        MapOperator<Invoice, Tuple2<String, Double>> result = null;
        LOG.info("TEST");
        try {
            result = data
                    // group by Invoices to get nichandles
                    .groupBy(Invoice::getNichandle)
                    // reduce the transactions
                    .reduce((ReduceFunction<Invoice>) (value1, value2) -> {
                        // display in logs for testing
                        LOG.debug("value1 "
                                          + value1.getNichandle()
                                          + " / value2 "
                                          + value2.getNichandle());

                        Double sum = value1.getTransaction() + value2.getTransaction();

                        LOG.info("TEST");
                        // return
                        return new Invoice(
                                value1.getNichandle(),
                                value1.getName(),
                                value1.getFirstName(),
                                value1.getRef(),
                                sum,
                                value1.getCurrency(),
                                value1.getZonedDate());
                    })
                    // map to tuple
                    // WARNING : this declaration is left as a non-lambda intentionnally
                    // as Type Erasure issues occur during the Lambda processing.
                    .map(new MapFunction<Invoice, Tuple2<String, Double>>() {
                        @Override
                        public Tuple2<String, Double> map(Invoice invoice) throws Exception {
                            LOG.info("TEST");
                            String nic;
                            Double sum;

                            // if USD, convert to EUR
                            if (invoice.getCurrency().equals("USD")) {
                                // implement currency conversion here
                                // sum = exRate.convertToEur(invoice.getTransaction());
                                sum = invoice.getTransaction();
                                nic = invoice.getNichandle()
                                        + invoice.getName()
                                        + invoice.getFirstName()
                                        + "(Previously " + invoice.getCurrency() + ")";
                            }
                            else {
                                sum = invoice.getTransaction();
                                nic = invoice.getNichandle()
                                        + invoice.getName()
                                        + invoice.getFirstName()
                                        + invoice.getCurrency();
                            }

                            return new Tuple2<>(nic, sum);
                        }
                    });
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return result;
    }
}
