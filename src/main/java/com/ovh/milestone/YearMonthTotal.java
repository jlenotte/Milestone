package com.ovh.milestone;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Calculate the total revenue for each month per year
 */
public class YearMonthTotal
{

    private static final Logger LOG = LoggerFactory.getLogger(YearMonthTotal.class);



    /**
     * Get total profit of each month per year
     */
    public ReduceOperator<Tuple2<String, Double>> getTotalPerYearMonth(DataSet<Invoice> data)
    {
        ReduceOperator<Tuple2<String, Double>> result = null;

        try
        {
            result = data
                // Map from Invoice -> Date & transaction
                .map(new MapFunction<Invoice, Tuple2<String, Double>>()
                {
                    @Override
                    public Tuple2<String, Double> map(Invoice value) throws Exception
                    {
                        ZonedDateTime zDate = value.getZonedDate();
                        String s = zDate.format(DateTimeFormatter.ofPattern("yyyy/MM"));
                        Double amount = value.getTransaction();
                        return new Tuple2<>(s, amount);
                    }
                })
                // Group dates by year/month (here we group by the Tuple2's index 0
                .groupBy(0)
                // Reduce to one value (a month of the year)
                .reduce(new ReduceFunction<Tuple2<String, Double>>()
                {
                    @Override
                    public Tuple2<String, Double> reduce(Tuple2<String, Double> value1, Tuple2<String, Double> value2) throws Exception
                    {
                        Double sum = value1.f1 + value2.f1;
                        return new Tuple2<>(value1.f0, sum);
                    }
                });
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
        return result;
    }



    /**
     * Get the best months
     */
    public GroupReduceOperator<Tuple2<String, Double>, Tuple2<String, Double>> getBestMonths(DataSet<Invoice> data, int limit)
    {
        GroupReduceOperator<Tuple2<String, Double>, Tuple2<String, Double>> result = null;

        try
        {
            result = data
                // Map from Invoice -> Date & transaction
                .map(new MapFunction<Invoice, Tuple2<String, Double>>()
                {
                    @Override
                    public Tuple2<String, Double> map(Invoice value) throws Exception
                    {
                        ZonedDateTime zDate = value.getZonedDate();
                        String s = zDate.format(DateTimeFormatter.ofPattern("yyyy/MM"));
                        Double amount = value.getTransaction();
                        return new Tuple2<>(s, amount);
                    }
                })
                // Group dates by year/month (here we group by the Tuple2's index 0
                .groupBy(0)
                // Reduce to one value (a month of the year)
                .reduce(new ReduceFunction<Tuple2<String, Double>>()
                {
                    @Override
                    public Tuple2<String, Double> reduce(Tuple2<String, Double> value1, Tuple2<String, Double> value2) throws Exception
                    {
                        Double sum = value1.f1 + value2.f1;
                        return new Tuple2<>(value1.f0, sum);
                    }
                })
                .sortPartition(1, Order.DESCENDING)
                .first(10);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
        return result;
    }
}
