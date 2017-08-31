package com.ovh.milestone.Conversion;

import com.ovh.milestone.Invoice;
import com.ovh.milestone.PerNicTotal;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.operators.JoinOperator.DefaultJoin;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * ExchangeRate
 *
 * Check the invoice's date and apply the correct currency ExRate
 *
 * 1. The date defines the variation of the currency 2. If weekend or off day, take the last known
 * ExRate 3. The currency conversion will be applied to the transaction field 4. The result will be
 * written into a new file
 *
 * EUR -> USD exchangeRate = 1.1937 (30/08/2017)
 */
public class ExchangeRate
{

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRate.class);



    /**
     * Convert from EUR to USD
     *
     * @param data (dataset of Invoices)
     * @return (a new dataset with USD prices)
     * @throws IOException ioe
     */
    public Double usdConversion(DataSet<Invoice> data) throws IOException
    {
        return null;
    }



    /**
     * Convert to USD
     */
    public Double convertToUsd(Double in)
    {
        return null;
    }



    /**
     * Convert to EUR
     */
    public Double convertToEur(Double in)
    {
        return null;
    }



    /**
     * Check if the date is a WE or off day
     *
     * /!\ wtf method /!\
     */
    public void checkDate()
    {

    }

}
