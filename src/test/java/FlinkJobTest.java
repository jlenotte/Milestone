import com.ovh.milestone.DataSource;
import com.ovh.milestone.FlinkJob;
import com.ovh.milestone.Invoice;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlinkJobTest
{
    final static Logger LOGGER = LoggerFactory.getLogger(FlinkJobTest.class);

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

        String csvFile = "dataBase.csv";
        DataSource ds = new DataSource();
        DataSet<String> fileName = env.readTextFile(csvFile);
        List<Invoice> list = new ArrayList<>(ds.readFile(fileName));
        FlinkJob fj = new FlinkJob();

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

    /*
    public DataSet<Invoice> getBestTransactions(List<Invoice> list)
    {

    }
    */
}
