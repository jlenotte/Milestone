import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ovh.milestone.DataSource;
import com.ovh.milestone.FlinkJob;
import com.ovh.milestone.Invoice;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
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

            fj.getNichandleSumFlink(data, 100);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }
}
