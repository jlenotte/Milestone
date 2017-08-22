package com.ovh.milestone;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.DataSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlinkJob
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkJob.class);

    /**
     *
     * Sort all the Invoices by order DESC
     *
     */
    public List<Invoice> getTopTransactions(List<Invoice> list, int amount)
    {
        // Init vars
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
     *
     *
     * Sort the data in data Sink
     *
     */
    public DataSink<String> getTopN(DataSet<String> data)
    {
        return data.writeAsCsv("DataSet_Result", "\n", ",");
    }

    /**
     * Display the list with flink
     */
    public static void displayContent(DataSet<String> text)
    {
        text
            .map((MapFunction<String, Invoice>) s -> {

                String[] splitter = s.split(",");

                // parse string to double
                double value = Double.parseDouble(splitter[3]);

                // parse string to zdate
                ZonedDateTime date = ZonedDateTime.parse(splitter[4]);

                return new Invoice(splitter[0], splitter[1], splitter[2], value, date);
            })
            .map(new MapFunction<Invoice, String>()
            {
                @Override
                public String map(Invoice invoice) throws Exception
                {
                    return invoice.toString();
                }
            }).writeAsText("/tmp/fffuuuuuu"); // once the collect is done, the process stops
    }
}
