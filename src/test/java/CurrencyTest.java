import static java.lang.Double.parseDouble;

import com.opencsv.CSVReader;
import com.ovh.milestone.Conversion.Currencies;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CurrencyTest
{

    private static final Logger LOG = LoggerFactory.getLogger(Currencies.class.getName());



    @Test
    public void csvToMap() throws IOException
    {

        Map<String, Double> map = new HashMap<>();

        // Read the CSV file
        try (CSVReader read = new CSVReader(new FileReader("/home/jlenotte/WORKSPACE/Milestone/dateLines.csv"), ','))
        {
            String[] line;

            // Read every line as long as it is not null
            while ((line = read.readNext()) != null)
            {
                // Check that our line is splitted correctly
                if (line.length >= 2)
                {
                    // Check that we have a key that is a DATE
                    // Check that we have a value that is a correct XRATE
                    if (line[0] != null && isCorrectDate(line[0]) &&
                        line[1] != null && isCorrectXrate(line[1]))
                    {
                        // If my K, V are ok, then I can put the info to the map
                        map.put(line[0], Double.parseDouble(line[1]));
                    }
                }
                String key = line[0];
                Double value = parseDouble(line[1]);

                if (line[1] == null)
                {
                    map.put(key, null);
                    read.readNext();
                }
                else
                {
                    map.put(key, value);
                    read.readNext();
                }
            }
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
    }



    private boolean isCorrectDate(String s)
    {
        return false;
    }



    private boolean isCorrectXrate(String s)
    {
        // Check that the Xrate is not null
        // Check that that the line's length is > 0
        if (s != null && s.trim().length() > 0)
        {
            try
            {
                Double.parseDouble(s);
                return true;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        return false;
    }



    @Test
    public void testMapElementVide()
    {

        // 2017/11/12  ""

        Map<String, Double> map = new HashMap<>();
        map.put("2017/12/12", 5.0d);

        System.out.println(map.get("zioefozefo"));

        if (map.get("2017/11/12") == null)
        {
            // date existe pas essaye date d'avant
            System.out.println("existe pas cherche avant");
        }
        else
        {
            // plop c gagneeee
            System.out.println("existe bingo");
        }
    }



    @Test
    public void csvToMapTest()
    {
        Currencies c = new Currencies();
        try
        {
            c.csvToMap();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
