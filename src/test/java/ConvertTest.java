import com.ovh.milestone.Invoice;
import com.ovh.milestone.Conversion.Convert;
import com.ovh.milestone.Conversion.ForexRate;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertTest {

    private static final Logger LOG = LoggerFactory.getLogger(Convert.class);



    @Test
    public void compareForex() throws IOException {

        // Setup properties
        ParameterTool config = ParameterTool.fromPropertiesFile("milestone.properties");

        // Setup Flink environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // Broadcast


        // Setup input data
        String csvFile = config.get("csvFile", "dataBase.csv");
        String forex = config.get("forex", "forex.csv");
        System.out.println(csvFile);

        // Setup output data
        String resultCsvFile = config.get("resultCsvFile");
        System.out.println(resultCsvFile);

        // Instances
        Convert conv = new Convert();

        // Read CSV file and convert to POJO
        // 1st dataset of invoices
        DataSet<Invoice> data1 = env.readCsvFile(csvFile)
                                    .pojoType(Invoice.class, "nichandle", "name", "firstName",
                                        "transaction", "currency", "date");

        // 2nd dataset of forex rates
        DataSet<ForexRate> data2 = env.readCsvFile(forex)
                                      .pojoType(ForexRate.class, "date", "forex");

        DataSet<Invoice> result = data1
            .join(data2)
            .where("date")
            .equalTo("date")
            .with(new JoinFunction<Invoice, ForexRate, Invoice>() {

                @Override
                public Invoice join(Invoice first, ForexRate second) throws Exception {

                    ZonedDateTime zDate = first.getZonedDate();
                    String billDate = zDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Double convertedSum = null;

                    // If both dates match, and currency is EUR, do the conversion EUR -> USD
                    if (billDate.equals(second.getDate()) && "EUR".equals(first.getCurrency())) {
                        // Convert
                        convertedSum = toUsd(second.getForex(), first.getTransaction());

                        return new Invoice(
                            first.getNichandle(),
                            first.getName(),
                            first.getFirstName(),
                            convertedSum,
                            "USD",
                            first.getZonedDate());

                    } else {
                        return new Invoice(
                            first.getNichandle(),
                            first.getName(),
                            first.getFirstName(),
                            first.getTransaction(),
                            first.getCurrency(),
                            first.getZonedDate());
                    }
                }
            });

        // return result;

    }



    public Double toUsd(Double xrate, Double sum) {

        Double result = null;

        // The xrate is gotten as input
        // Check that the xrate is > 0 and not negative & convert
        if (xrate > 0 && xrate != null) {
            result = sum * xrate;
        }

        return result;
    }



    @Test
    public void testConversion() throws IOException {
        // Setup properties
        ParameterTool config = ParameterTool.fromPropertiesFile("milestone.properties");

        // Setup Flink environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // Setup input data
        String csvFile = config.get("csvFile", "dataBase.csv");
        String forex = config.get("forex", "forex.csv");
        System.out.println(csvFile);

        // Setup output data
        String resultCsvFile = config.get("resultCsvFile");
        System.out.println(resultCsvFile);

        // Instances
        Convert conv = new Convert();

        // Read CSV file and convert to POJO
        // 1st dataset of invoices
        DataSet<Invoice> data1 = env.readCsvFile(csvFile)
                                    .pojoType(Invoice
                                            .class,
                                        "nichandle",
                                        "name", "firstName",
                                        "transaction", "currency", "date");

        // 2nd dataset of forex rates
        DataSet<ForexRate> data2 = env.readCsvFile(forex)
                                      .pojoType(ForexRate.class, "date", "forex");

        try {
            DataSet<Invoice> result = conv.convertForexBroadcast(data1, data2);
            result.writeAsText(resultCsvFile, FileSystem.WriteMode.OVERWRITE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

    }
}
