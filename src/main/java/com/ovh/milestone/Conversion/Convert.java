package com.ovh.milestone.Conversion;

import com.ovh.milestone.Invoice;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Join an Invoice Dataset and a Date/ForexRate Dataset to convert according to the right date
 */

public class Convert extends ForexProcessor {


    private static final transient Logger LOG = LoggerFactory.getLogger(Convert.class);




    /**
     * Converts with forex rates
     *
     * @param data1 DataSet
     * @param data2 DataSet
     * @return DataSet
     * @throws IOException ioe
     */
    public DataSet<Invoice> convertForexBroadcast(final DataSet<Invoice> data1, final
    DataSet<ForexRate>
            data2) throws IOException {

        return data1.map(new RichMapFunction<Invoice, Invoice>() {

            transient Map<String, ForexRate> broadcastedSet = new TreeMap<String, ForexRate>();


            /*
            @Override
            public void open(Configuration parameters) throws Exception {
                // Access the broadcast dataset as a collection
                broadcastedSet = getRuntimeContext().getBroadcastVariable
                    ("broadcastSetName");
            }
            */




            @Override
            public Invoice map(Invoice value) throws Exception {
                String date = value.getZonedDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).trim();

                ForexRate xrateOfTheDay = broadcastedSet.get(date);
                Double xrate = xrateOfTheDay.getForex();

                // conversion
                String newCurr = value.getNewCurrency();
                Double convertedValue = xrate * value.getTransaction();
                LOG.info(String.valueOf(convertedValue));

                return new Invoice(value.getNichandle(), value.getName(), value.getFirstName(),
                                   value.getTransaction(), value.getCurrency(), newCurr, date, convertedValue);
            }
        }).withBroadcastSet(data2, "broadcastSetName");
    }




    /**
     * Join an Invoice Dataset and a Date/ForexRate Dataset to convert according to the right date
     *
     * @param data1 DataSet
     * @param data2 DataSet
     * @return DataSet
     * @throws IOException IOException
     */
    public DataSet<Invoice> convertForex(final DataSet<Invoice> data1, final
    DataSet<ForexRate> data2) throws IOException {

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
     *
     * @param xrate Double
     * @param sum   Double
     * @return Double
     */
    public static Double toUsd(Double xrate, Double sum) {

        Double result = null;

        // The xrate is gotten as input
        // Check that the xrate is > 0 & convert
        if (xrate > 0) {
            result = sum * xrate;
        }

        return result;
    }
}
