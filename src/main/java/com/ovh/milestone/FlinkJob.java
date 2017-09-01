package com.ovh.milestone;

import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSink;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlinkJob {

    private static final Logger LOG = LoggerFactory.getLogger(FlinkJob.class);



    /**
     * Get top N transactions with Stream
     */
    public List<Invoice> getTopTransactions(List<Invoice> list, int amount) {
        // Boilerplate
        List<Invoice> resultList;
        Comparator<Invoice> byTransaction;

        // Define comparator to double for transactions
        byTransaction = Comparator.comparingDouble(Invoice::getTransaction);

        // Sort the list and limit N
        resultList = list
            .parallelStream()
            .sorted(byTransaction.reversed())
            .limit(amount)
            .collect(Collectors.toList());

        return resultList;
    }



    /**
     * Get top N transactions with Flink method 1 In progress...
     */
    public DataSink<Invoice> getTopTransactionsFlink(ExecutionEnvironment env,
                                                     DataSet<Invoice> data, int limit) {
        // Sort & limit the data
        return data
            .map((MapFunction<Invoice, Invoice>) invoice -> invoice)
            .sortPartition(0, Order.DESCENDING)
            .writeAsCsv("SortResult.csv");
    }



    /**
     * Get transactions totals with Flink
     *
     * @param data input DataSet
     * @param limit input Limiter
     * @return DataSet of Invoice
     * @deprecated outdated method and pojo conversion
     */
    @Deprecated
    public DataSet<Invoice> getTransactionTotalsFlink(DataSet<String> data, int limit) {

        // Convert to POJO
        return data
            .map((MapFunction<String, Invoice>) s ->
            {
                String[] splitter = s.split(",");

                // parse string to double
                double value = Double.parseDouble(splitter[3]);

                // curr
                String currency = splitter[4];

                // parse string to zdate
                ZonedDateTime date = ZonedDateTime.parse(splitter[5]);

                // assign values to object params

                return new Invoice(splitter[0], splitter[1], splitter[2],
                    value, currency, date);
            })
            // Group by Invoices to get nichandles
            .groupBy(Invoice::getNichandle)
            // Reduce the transactions
            .reduce((ReduceFunction<Invoice>) (invoice, t1) -> new Invoice(invoice.getNichandle(),
                invoice.getName(),
                invoice.getFirstName(),
                invoice.getTransaction() + t1.getTransaction(),
                invoice.getCurrency(),
                invoice.getZonedDate()))
            // Map to Tuple
            .map((MapFunction<Invoice, Tuple2<String, Double>>) invoice ->
            {
                String uid = invoice.getNichandle()
                    + invoice.getName()
                    + invoice.getFirstName();
                Double sum = invoice.getTransaction();
                return new Tuple2<>(uid, sum);
            })
            // Sort and limit
            .sortPartition(1, Order.DESCENDING)
            .first(limit)
            .map((MapFunction<Tuple2<String, Double>, Invoice>) stringDoubleTuple2 ->
                new Invoice());
    }



    /**
     * Filter sample
     *
     * @deprecated filtering sample for educational purposes
     */
    @Deprecated
    public DataSet<Invoice> getTransactionsPerMonthFlink(DataSet<Invoice> data) {
        return data
            // filter by wanted year & month
            .filter(new FilterFunction<Invoice>() {
                @Override
                public boolean filter(Invoice value) throws Exception {
                    return value.getZonedDate().getMonth() == Month.JANUARY;
                }
            });
    }
}
