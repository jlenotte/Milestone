package com.ovh.milestone.FlinkJobs;

import com.ovh.milestone.Invoice;

import java.io.IOException;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Revenue executes an aggregation of all transactions and calculates the total Revenue
 */

public class Revenue {


    private static final Logger LOG = LoggerFactory.getLogger(Revenue.class);




    public UnsortedGrouping<Invoice> totalRevenue(DataSet<Invoice> data1) throws IOException {

        // Calculate the revenue of the entire month
        UnsortedGrouping<Invoice> result = null;
        try {

        }
        catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return result;
    }
}
