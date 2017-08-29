package com.ovh.milestone;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.JoinOperator.DefaultJoin;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinDatasets implements JoinFunction<Invoice, Tuple2<String, Double>, Tuple2<String, Double>>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinDatasets.class);



    /**
     * Join two datasets
     */
    public DefaultJoin<Invoice, Invoice> joinSets(DataSet<Invoice> data1, DataSet<Invoice> data2)
    {
        return data1.join(data2)
                    .where("nichandle")
                    .equalTo("nichandle");
    }



    @Override
    public Tuple2<String, Double> join(Invoice first, Tuple2<String, Double> second) throws Exception
    {
        return new Tuple2<>(first.getNichandle(), first.getTransaction() + second.f1);
    }



    /**
     * Union two datasets
     */
    public DataSet<Invoice> unionSets(DataSet<Invoice> data1, DataSet<Invoice> data2)
    {
        return data1.union(data2);
    }

    /**
     *
     */
}