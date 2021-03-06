package com.ovh.milestone.FlinkJobs;

import com.ovh.milestone.Invoice;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.JoinOperator.DefaultJoin;
import org.apache.flink.api.java.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Join and Union of two DataSets
 */

public class JoinDatasets implements
                          JoinFunction<Invoice, Tuple2<String, Double>, Tuple2<String, Double>> {


    private static final Logger LOG = LoggerFactory.getLogger(JoinDatasets.class);




    /**
     * Join two DataSets
     *
     * @param data1 DataSet1
     * @param data2 DataSet2
     * @return JoinOperator
     */
    public DefaultJoin<Invoice, Invoice> joinSets(DataSet<Invoice> data1, DataSet<Invoice> data2) {
        return data1.join(data2)
                .where("nichandle")
                .equalTo("nichandle");
    }




    @Override
    public Tuple2<String, Double> join(Invoice first, Tuple2<String, Double> second) throws Exception {
        return new Tuple2<>(first.getNichandle(), first.getTransaction() + second.f1);
    }




    /**
     * Union of DataSets
     *
     * @param data1
     * @param data2
     * @return
     */
    public DataSet<Invoice> unionSets(DataSet<Invoice> data1, DataSet<Invoice> data2) {
        return data1.union(data2);
    }

}