import com.ovh.milestone.DataSource;
import com.ovh.milestone.FlinkJob;
import com.ovh.milestone.Invoice;
import com.ovh.milestone.FlinkJobs.JoinDatasets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.JoinOperator.DefaultJoin;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlinkJobTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkJobTest.class);



    @Test
    public void top3Test() throws IOException
    {
        //Program "skeleton"
        //Environment variables
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource ds = new DataSource();
        DataSet<String> data = env.readTextFile("dataBase.csv");
        List<Invoice> list = new ArrayList<>(ds.readFile(data));
        FlinkJob fj = new FlinkJob();

        try
        {
            fj.getTopTransactions(list, 100);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }



    @Test
    public void topNTest() throws IOException
    {
        //Environment variables
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //Program "skeleton"

        DataSource ds = new DataSource();
        String csvFile = "dataBase.csv";
        DataSet<String> fileName = env.readTextFile(csvFile);
        FlinkJob fj = new FlinkJob();
        List<Invoice> list = new ArrayList<>(ds.readFile(fileName));

        try
        {
            List<Invoice> result = fj.getTopTransactions(list, 10);
            for (Invoice invoice2 : result)
            {
                LOGGER.info(invoice2.toString());
            }

        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }



    @Test
    public void getTransactionTotals()
    {
        try
        {
            // Boilerplate
            final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

            env.registerType(Invoice.class);
            String csvFile = "dataBase.csv";
            DataSet<Invoice> data = env.readCsvFile(csvFile)
                                       .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");
            FlinkJob fj = new FlinkJob();

//            MapOperator<Invoice, Tuple2<String, Double>> result = fj.getNichandleSumFlink(data);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }




    @Test
    public void perMonthOfYearTest()
    {
        try
        {
            // Boilerplate
            final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

            env.registerType(Invoice.class);
            String csvFile = "dataBase.csv";
            DataSet<Invoice> data = env.readCsvFile(csvFile)
                                       .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");
            FlinkJob fj = new FlinkJob();

//            fj.getNichandleSumFlink(data);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }



    @Test
    public void joindata()
    {
        // Boilerplate
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        env.registerType(Invoice.class);
        String csvFile = "dataBase.csv";
        String csvFile2 = "dataBase2.csv";
        DataSet<Invoice> data1 = env.readCsvFile(csvFile)
                                    .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");
        DataSet<Invoice> data2 = env.readCsvFile(csvFile2)
                                    .pojoType(Invoice.class, "nichandle", "name", "firstName", "transaction", "date");

        JoinDatasets jd = new JoinDatasets();
        DefaultJoin<Invoice, Invoice> result = jd.joinSets(data1, data2);


    }


}
