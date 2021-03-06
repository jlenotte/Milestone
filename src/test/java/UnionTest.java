import com.ovh.milestone.Invoice;
import com.ovh.milestone.FlinkJobs.JoinDatasets;
import com.ovh.milestone.FlinkJobs.PerNicTotal;
import com.ovh.milestone.FlinkJobs.TopCustomers;
import com.ovh.milestone.FlinkJobs.YearMonthTotal;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.junit.Test;

public class UnionTest
{

    @Test
    public void unionTest() throws Exception
    {
        // Setup properties
        ParameterTool config = ParameterTool.fromPropertiesFile("milestone.properties");

        // Setup Flink environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // Setup input data
        String csvFile = config.get("csvFile", "dataBase.csv");
        String csvFile2 = config.get("csvFile2", "dataBase2.csv");
        System.out.println(csvFile);

        // Setup output data
        String resultFile = config.get("resultFile");
        String resultCsvFile = config.get("resultCsvFile");
        System.out.println(resultCsvFile);

        int limit10 = Integer.parseInt(config.get("limit10"));
        int limit100 = Integer.parseInt(config.get("limit100"));
        int limit1000 = Integer.parseInt(config.get("limit1000"));

        // Instances

        YearMonthTotal yearMonthTotal = new YearMonthTotal();
        PerNicTotal nicTotal = new PerNicTotal();
        TopCustomers topCusts = new TopCustomers();
        JoinDatasets jd = new JoinDatasets();

        // Read CSV file and convert to POJO
        DataSet<Invoice> data = env.readCsvFile(csvFile)
                                   .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");

        DataSet<Invoice> data2 = env.readCsvFile(csvFile2)
                                    .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");

        // Union
        DataSet<Invoice> result = jd.unionSets(data, data2);

        // Get the sum of all transactions per year/MM
        ReduceOperator<Tuple2<String, Double>> result2 = yearMonthTotal.getTotalPerYearMonth(data);

        // Get the result in a DataSink
        result2.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);

        env.execute();
    }
}
