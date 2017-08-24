package com.ovh.milestone;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{

    /**
     *
     * Program Milestone is a revamp of the project Charlotte
     * which is basically a program that processes DataSets
     * in order to get data results like top N transactions for
     * a specific month, or year, etc ...
     *
     * Milestone will be the first step to a new real Flink
     * project
     *
     * Useful links :
     * https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html#data-sources
     * https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html
     * https://cwiki.apache.org/confluence/display/FLINK/Best+Practices+and+Lessons+Learned
     * https://ci.apache.org/projects/flink/flink-docs-release-1.2/dev/batch/dataset_transformations.html#sort-partition
     * https://ci.apache.org/projects/flink/flink-docs-release-1.3/dev/batch/index.html /!\/!\/!\/!\
     *
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args)
    {

        // Boilerplate
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        String csvFile = "dataBase.csv";
        DataSet<String> data = env.readTextFile(csvFile);
        FlinkJob fj = new FlinkJob();

        //Method calls

        try
        {
            LOGGER.info("TEST");
            //fj.getTransactionTotalsFlink(data, 100);
            // Once a Data Sink will be created, this will be used to write or print the Dataset
            // env.execute();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

    }
}
