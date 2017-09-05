package com.ovh.milestone.conversion;

import com.ovh.milestone.Invoice;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.JoinOperator.EquiJoin;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can be called to convert the sum of an invoice, a transaction
 */

public class Convert extends ForexProcessor {

    private static final transient Logger LOG = LoggerFactory.getLogger(Convert.class);



    public DataSet<Invoice> convertForex(final DataSet<Invoice> data1, final DataSet<ForexRate>
        data2) throws IOException {

        return data1
            .map(new MapFunction<Invoice, Invoice>() {
                @Override
                public Invoice map(Invoice value) throws Exception {
                    String date = value.getZonedDate()
                                       .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).trim();

                    return new Invoice(value.getNichandle(), value.getName(), value.getFirstName(),
                        value.getTransaction(), value.getCurrency(), date);
                }
            })
            .join(data2)
            .where("date")
            .equalTo("date")
            .with(new ForexProcessor());
    }



    /**
     * Convert from EUR to USD according to the right date's currency
     */
    public static Double toUsd(Double xrate, Double sum) {

        Double result = null;

        // The xrate is gotten as input
        // Check that the xrate is > 0 and not negative & convert
        if (xrate > 0) {
            result = sum * xrate;
        }

        return result;
    }
}
