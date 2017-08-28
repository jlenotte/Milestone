package com.ovh.milestone;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.api.java.tuple.Tuple2;

public class Main
{

    /**
     * Program Milestone is a revamp of the project Charlotte which is basically a program that
     * processes CSV files into DataSets to get results like top N/total transactions for a specific month or year, etc ...
     *
     * Milestone will be the first step to a new real Flink project
     *
     * Useful links : https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html#data-sources
     * https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html
     * https://cwiki.apache.org/confluence/display/FLINK/Best+Practices+and+Lessons+Learned
     * https://ci.apache.org/projects/flink/flink-docs-release-1.2/dev/batch/dataset_transformations.html#sort-partition
     * https://ci.apache.org/projects/flink/flink-docs-release-1.3/dev/batch/index.html
     *
     * Install Flink for windows : https://ci.apache.org/projects/flink/flink-docs-release-1.3/setup/flink_on_windows.html
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);



    public static void main(String[] args) throws Exception
    {
        // Setup properties
        String configFile = "milestone.properties";
        ParameterTool config = ParameterTool.fromPropertiesFile(configFile);


        // Boilerplate
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //
        String csvFile = config.get("csvFile", "data.csv");
        System.out.println(csvFile);

        //
        String resultFile = config.get("resultFile");

        //
        String resultCsvFile = config.get("resultCsvFile");
        System.out.println(resultCsvFile);

        int limit10 = Integer.parseInt(config.get("limit10"));
        int limit100 = Integer.parseInt(config.get("limit100"));
        int limit1000 = Integer.parseInt(config.get("limit1000"));


        // Instances

        YearMonthTotal yearMonthTotal = new YearMonthTotal();
        PerNicTotal nicTotal = new PerNicTotal();
        TopCustomers topCusts = new TopCustomers();

        // Read CSV file and convert to POJO
        DataSet<Invoice> data = env.readCsvFile(csvFile)
                                   .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");


        // Try to run the code
        try
        {

            // Get the total of transactions per nichandle
            MapOperator<Invoice, Tuple2<String, Double>> result = nicTotal
                .getNichandleSumFlink(data);

            // Get the sum of all transactions per year/MM
            // ReduceOperator<Tuple2<String, Double>> result = yearMonthTotal.getTotalPerYearMonth(data);

            // Get top customers
            // GroupReduceOperator result = topCusts.getTopCustomersByNic(data, limit100);

            // Get top months
            // GroupReduceOperator result = yearMonthTotal.getBestMonths(data, 100);


            // Get the result in a DataSink
            result.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);

        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

        // Execute
        env.execute();
    }
}
