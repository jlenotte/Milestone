package com.ovh.milestone;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopCustomers
{

    private static final Logger LOGGER = LoggerFactory.getLogger(YearMonthTotal.class);



    /**
     * Get the 100 best customers (name + transaction)
     *
     * @param data Takes a DataSet in
     * @param limit limits the operation to the 100 first elements
     * @return Returns a GroupReduceOperator
     */
    public GroupReduceOperator getTopCustomers(DataSet<Invoice> data, int limit)
    {
        GroupReduceOperator result = null;

        try
        {
            result = data
                // Map from Invoice to Name + transaction
                .map(new MapFunction<Invoice, Tuple2<String, Double>>()
                {
                    @Override
                    public Tuple2<String, Double> map(Invoice value) throws Exception
                    {
                        String custName = value.getName() + " " + value.getFirstName();
                        Double transaction = value.getTransaction();
                        return new Tuple2<>(custName, transaction);
                    }
                })
                // GroupBy customer's name (Tuple's field 0)
                .groupBy(0)
                // Reduce
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
                .first(limit);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

        return result;
    }



    /**
     * Get the 100 best customers (nic + transaction)
     *
     * @param data Takes a DataSet in
     * @param limit limits the operation to the 100 first elements
     * @return Returns a GroupReduceOperator
     */
    public GroupReduceOperator<Tuple2<String, Double>, Tuple2<String, Double>> getTopCustomersByNic(DataSet<Invoice> data, int limit)
    {
        GroupReduceOperator<Tuple2<String, Double>, Tuple2<String, Double>> result = null;

        try
        {
            result = data
                // Map from Invoice to Name + transaction
                .map(new MapFunction<Invoice, Tuple2<String, Double>>()
                {
                    @Override
                    public Tuple2<String, Double> map(Invoice value) throws Exception
                    {
                        String nic = value.getNichandle()
                            + "("
                            + value.getName()
                            + " "
                            + value.getFirstName()
                            + ")";
                        Double transaction = value.getTransaction();
                        return new Tuple2<>(nic, transaction);
                    }
                })
                // GroupBy customer's name (Tuple's field 0)
                .groupBy(0)
                // Reduce
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
                .first(limit);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

        return result;
    }
}
