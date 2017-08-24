package com.ovh.milestone;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

public class FlinkJob
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkJob.class);



    /**
     * Get top N transactions with Stream
     */
    public List<Invoice> getTopTransactions(List<Invoice> list, int amount)
    {
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
    public DataSink<Invoice> getTopTransactionsFlink(ExecutionEnvironment env, DataSet<Invoice> data, int limit)
    {
        // Sort & limit the data
        return data
            .map((MapFunction<Invoice, Invoice>) invoice -> invoice)
            .sortPartition(0, Order.DESCENDING)
            .writeAsCsv("SortResult.csv");
    }



    /**
     *
     * Get transactions totals with Flink
     *
     */
    public DataSet<Invoice> getTransactionTotalsFlink(DataSet<String> data, int limit)
    {

        // Convert to POJO
        return data
            .map((MapFunction<String, Invoice>) s ->
            {
                String[] splitter = s.split(",");

                // parse string to double
                double value = Double.parseDouble(splitter[3]);

                // parse string to zdate
                ZonedDateTime date = ZonedDateTime.parse(splitter[4]);

                // assign values to object params

                return new Invoice(splitter[0], splitter[1], splitter[2],
                    value, date);
            })
            // Group by Invoices to get nichandles
            .groupBy(Invoice::getNichandle)
            // Reduce the transactions
            .reduce((ReduceFunction<Invoice>) (invoice, t1) -> new Invoice(invoice.getNichandle(),
                invoice.getName(),
                invoice.getFirstName(),
                invoice.getTransaction() + t1.getTransaction(),
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
     *
     * Get the sum of all transactions per nic
     *
     */
    public DataSet<Invoice> getNichandleSumFlink(DataSet<Invoice> data, int limit)
    {
        return data
            // group by Invoices to get nichandles
            .groupBy(Invoice::getNichandle)
            // reduce the transactions
            .reduce(new ReduceFunction<Invoice>()
            {
                @Override
                public Invoice reduce(Invoice value1, Invoice value2) throws Exception
                {
                    return new Invoice(value1.getNichandle(),
                        value1.getName(),
                        value1.getFirstName(),
                        value1.getTransaction() + value2.getTransaction(),
                        value1.getZonedDate());
                }
            })
            // map to tuple
            .map(new MapFunction<Invoice, Tuple2<String, Double>>()
            {
                @Override
                public Tuple2<String, Double> map(Invoice invoice) throws Exception
                {
                    String nic = invoice.getNichandle()
                        + invoice.getName()
                        + invoice.getFirstName();
                    Double sum = invoice.getTransaction();
                    Tuple2<String, Double> tuple = new Tuple2<>(nic, sum);
                    return tuple;
                }
            })
            .map(new MapFunction<Tuple2<String, Double>, Invoice>()
            {
                @Override
                public Invoice map(Tuple2<String, Double> value) throws Exception
                {
                    Invoice inv = new Invoice();
                    return inv;
                }
            });
    }


    /**
     * Display the list with flink
     */
    @Deprecated
    public void displayContent(DataSet<String> text)
    {
        text
            .map((MapFunction<String, Invoice>) s ->
            {

                String[] splitter = s.split(",");

                // parse string to double
                double value = Double.parseDouble(splitter[3]);

                // parse string to zdate
                ZonedDateTime date = ZonedDateTime.parse(splitter[4]);

                return new Invoice(splitter[0], splitter[1], splitter[2], value, date);
            })
            .map((MapFunction<Invoice, String>) Invoice::toString)
            .writeAsText("/tmp/fffuuuuuu"); // once the collect is done, the process stops
    }
}
