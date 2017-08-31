package com.ovh.milestone;

import com.ovh.milestone.Conversion.ConversionLine;
import com.ovh.milestone.Conversion.Currencies;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.operators.JoinOperator.DefaultJoin;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.api.java.operators.SortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{

    /**
     * Program Milestone is a revamp of the project Charlotte which is basically a program that
     * processes CSV files into new DataSets
     *
     * Milestone will be the first step to a new Flink project
     *
     * Useful links : https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html#data-sources
     * https://ci.apache.org/projects/flink/flink-docs-release-0.8/programming_guide.html
     * https://cwiki.apache.org/confluence/display/FLINK/Best+Practices+and+Lessons+Learned
     * https://ci.apache.org/projects/flink/flink-docs-release-1.2/dev/batch/dataset_transformations.html#sort-partition
     * https://ci.apache.org/projects/flink/flink-docs-release-1.3/dev/batch/index.html
     *
     * Install Flink for windows : https://ci.apache.org/projects/flink/flink-docs-release-1.3/setup/flink_on_windows.html
     */

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String PROPERTIES = "/home/jlenotte/WORKSPACE/Milestone/src/main/resources/milestone.properties";



    public static void main(String[] args) throws Exception
    {

        // Setup properties
        ParameterTool config = ParameterTool.fromPropertiesFile(PROPERTIES);

        // Setup Flink environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // Setup input data
        String csvFile = config.get("csvFile3", "dataBase3.csv");
        String csvFile2 = config.get("csvFile2", "dataBase2.csv");
        String csvFile3 = config.get("csvFile4", "dataLines.csv");
        System.out.println(csvFile);

        // Setup output data
        String resultFile = config.get("resultFile");
        String resultCsvFile = config.get("resultCsvFile");
        System.out.println(resultCsvFile);

        int limit = Integer.parseInt(config.get("limit"));

        // Instances

        YearMonthTotal yearMonthTotal = new YearMonthTotal();
        PerNicTotal nicTotal = new PerNicTotal();
        TopCustomers topCusts = new TopCustomers();
        JoinDatasets jd = new JoinDatasets();
        Currencies curr = new Currencies();

        // Read CSV file and convert to POJO
        DataSet<Invoice> data = env.readCsvFile(csvFile)
                                   .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "currency", "date");

        DataSet<Invoice> data2 = env.readCsvFile(csvFile2)
                                    .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "currency", "date");

        DataSet<ConversionLine> currData = env.readCsvFile(csvFile3)
                                              .pojoType(ConversionLine.class, "date", "sum");

        String choice = config.get("choice");

        switch (choice)
        {
            case "join":
            {
                // Join
                DefaultJoin<Invoice, Invoice> result = jd.joinSets(data, data2);

                // Get the result in a DataSink
                result.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);
                break;
            }
            case "union":
            {
                // Union
                DataSet<Invoice> result = jd.unionSets(data, data2);

                // Get the result in a DataSink
                result.writeAsText(resultCsvFile, WriteMode.OVERWRITE);
                break;
            }
            case "topcust":
            {
                // Get top customers
                GroupReduceOperator result2 = topCusts.getTopCustomersByNic(data, limit);

                // Get the result in a DataSink
                result2.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);
                break;
            }
            case "pernictot":
            {
                // Get the total of transactions per nichandle
                MapOperator<Invoice, Tuple2<String, Double>> result2 = nicTotal.getNichandleSumFlink(data);

                // Get the result in a DataSink
                result2.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);
                break;
            }
            case "peryearmmtot":
            {
                // Get the sum of all transactions per year/MM
                ReduceOperator<Tuple2<String, Double>> result2 = yearMonthTotal.getTotalPerYearMonth(data);

                // Get the result in a DataSink
                result2.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);
                break;
            }
            case "currencymapping":
            {

                break;
            }

            default:
            {
                break;
            }
        }

        /*
        // Try to run the code
        try
        {
            // Join
            // DefaultJoin<Invoice, Invoice> result = jd.joinSets(data, data2);

            // Union
            DataSet<Invoice> result = jd.unionSets(data, data2);

            // Get the total of transactions per nichandle
            // MapOperator<Invoice, Tuple2<String, Double>> result2 = nicTotal.getNichandleSumFlink(result);

            // Get top customers
            GroupReduceOperator result2 = topCusts.getTopCustomersByNic(result, limit100);

            // Get top months
            // GroupReduceOperator result = yearMonthTotal.getBestMonths(data, 100);

            // Get the sum of all transactions per year/MM
            // ReduceOperator<Tuple2<String, Double>> result2 = yearMonthTotal.getTotalPerYearMonth(result);

            // Get the result in a DataSink
            result2.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);


        }

        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
        */

        // Execute
        env.execute();
    }
}
