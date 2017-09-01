import static org.junit.Assert.assertTrue;

import com.opencsv.CSVReader;
import com.ovh.milestone.util.ExchangeRates;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExchangeRatesTest {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRates.class.getName());

    // This map will contain dates(Strings) as Key
    // and FOREX rates (Doubles) as Values
    public static final Map<String, Double> map = new TreeMap<>();



    @Test
    public void csvToMap() throws IOException {

        Double lastKnownXRate = null;

        // Read the CSV file
        try (CSVReader read = new CSVReader(new FileReader
            ("/home/jlenotte/WORKSPACE/Milestone/dataLines3.csv"), ',')) {
            String[] line;

            // Read every line as long as it is not null
            while ((line = read.readNext()) != null) {

                // Check that our line is splitted correctly
                if (line.length >= 2) {

                    // Check that we have a correct K & V
                    if (line[0] != null && isCorrectDate(line[0]) && isCorrectXRate(line[1])) {

                        // If the Xrate is null, set the last known xrate
                        if (line[1].isEmpty()) {

                            // Add the values to the map
                            map.put(line[0], lastKnownXRate);

                        } else {

                            // If my K, V are ok, then I can put the info to the map
                            map.put(line[0], Double.parseDouble(line[1]));

                            // Add to temporary variable for off days
                            // Set off days to previous known date
                            lastKnownXRate = Double.parseDouble(line[1]);

                        }
                    }
                }
            }

            displayMap(map);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }



    /**
     * display the map
     */
    private void displayMap(final Map<String, Double> map) {
        for (Entry<String, Double> stringDoubleEntry : map.entrySet()) {
            LOG.debug(String.valueOf(stringDoubleEntry));
        }
    }



    /**
     * Make sure that the date has a correct format
     */
    private boolean isCorrectDate(String s) {

        // Check that the date contains 10 chars and matches the following regex :
        // "0000-00-00" aka yyyy/MM/dd
        if (s != null && s.trim().length() == 10 && s.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
            return true;
        }
        return false;
    }



    /**
     * Format date
     */
    public String formatDate(String s) throws Exception {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat correctDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = null;

        // If the literal matches the variable, format the date
        if ("([0-9]{2})-([0-9]{2})-([0-9]{4})".matches(s)) {
            newDate = correctDateFormat.format(df.parse(s));
        }
        return newDate;
    }



    /**
     * Make sure that the Xrate is not problematic /!\ this is very important as Xrates that are
     * equal to 0 will completely mess up the process and program /!\
     */
    private boolean isCorrectXRate(String s) throws Exception {
        // Check that that the line's length is > 0 aftre a trim
        // to avoid having unwanted spaces or special characters
        // that may make the length over 0
        if (s.trim().length() > 0) {
            // if correct, return true
            return true;
        }
        // here we still return true regardless to be able
        // to add the last known Xrate in case of a null value
        return true;
    }



    @Test
    public void csvToMapTest() {
        ExchangeRates c = new ExchangeRates();
        try {
            c.csvToMap("/home/jlenotte/WORKSPACE/Milestone/dataLines3.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
