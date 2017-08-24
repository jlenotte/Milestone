package com.ovh.milestone;

import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

public class Main
{

    /**
     * Program Milestone is a revamp of the project Charlotte which is basically a program that
     * processes DataSets in order to get data results like top N transactions for a specific month,
     * or year, etc ...
     *
     * Milestone will be the first step to a new real Flink project
     *
     * Useful links : https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html#data-sources
     * https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html
     * https://cwiki.apache.org/confluence/display/FLINK/Best+Practices+and+Lessons+Learned
     * https://ci.apache.org/projects/flink/flink-docs-release-1.2/dev/batch/dataset_transformations.html#sort-partition
     * https://ci.apache.org/projects/flink/flink-docs-release-1.3/dev/batch/index.html
     * /!\/!\/!\/!\
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);




    public static void main(String[] args)
    {

        // Boilerplate
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        String csvFile = "/home/jlenotte/WORKSPACE/Milestone/test.csv";
        FlinkJob fj = new FlinkJob();
        DataSet<Invoice> data = env.readCsvFile(csvFile)
                                   .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");

        // Try to run the code
        try
        {
            MapOperator<Invoice, Tuple2<String, Double>> result = fj.getNichandleSumFlink(data);
            result.writeAsText("/home/jlenotte/WORKSPACE/Milestone/toto/", FileSystem.WriteMode.OVERWRITE);

            env.execute();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }
}
