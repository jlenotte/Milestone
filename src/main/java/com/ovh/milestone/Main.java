package com.ovh.milestone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{

    /**
     *
     * Program Milestone is a revamp of the project Charlotte
     * which is basically a program that does filtering,
     * mapping, reducing, using Flink and Java Lambdas
     *
     * Milestone will be the first step to a new real Flink
     * project
     *
     * Useful links :
     * https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html#data-sources
     * https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html
     * https://cwiki.apache.org/confluence/display/FLINK/Best+Practices+and+Lessons+Learned
     * https://ci.apache.org/projects/flink/flink-docs-release-1.2/dev/batch/dataset_transformations.html#sort-partition
     *
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args)
    {


        //Program "skeleton"
        //Environment variables

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource ds = new DataSource();
        DataSet<String> data = env.readTextFile("dataBase.csv");
        List<Invoice> list = new ArrayList<>(ds.readFile(data));
        FlinkJob fj = new FlinkJob();

        //Method calls

        try
        {
            fj.getTopTransactions(list, 100);
            LOGGER.info("TEST");

            // Once a Data Sink will be created, this will be used to write or print the Dataset
            // env.execute();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

    }
}
