import com.ovh.milestone.Conversion.ExchangeRate;
import com.ovh.milestone.Invoice;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.Test;

public class CheckDateTest
{

    @Test
    public void checkDateTest()
    {
        ExchangeRate er = new ExchangeRate();
        er.checkDate();
    }
}
