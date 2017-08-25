package com.ovh.milestone;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerNicTotal
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PerNicTotal.class);



    /**
     * Get the sum of all transactions per nic
     */
    public MapOperator<Invoice, Tuple2<String, Double>> getNichandleSumFlink(DataSet<Invoice> data)
    {
        MapOperator<Invoice, Tuple2<String, Double>> result = null;

        try
        {
            result = data
                // group by Invoices to get nichandles
                .groupBy(Invoice::getNichandle)
                // reduce the transactions
                .reduce((ReduceFunction<Invoice>) (value1, value2) ->
                {
                    // display in logs for testing
                    LOGGER.debug("value1 "
                        + value1.getNichandle()
                        + " / value2 "
                        + value2.getNichandle());
                    // return
                    return new Invoice(
                        value1.getNichandle(),
                        value1.getName(),
                        value1.getFirstName(),
                        value1.getTransaction() + value2.getTransaction(),
                        value1.getZonedDate());
                })
                // map to tuple
                // WARNING : this declaration is left as a non-lambda intentionnally
                // as Type Erasure issues occur during the Lambda processing.
                .map((MapFunction<Invoice, Tuple2<String, Double>>) invoice ->
                {
                    String nic = invoice.getNichandle()
                        + invoice.getName()
                        + invoice.getFirstName();

                    Double sum = invoice.getTransaction();

                    return new Tuple2<>(nic, sum);
                });
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

        return result;
    }
}
