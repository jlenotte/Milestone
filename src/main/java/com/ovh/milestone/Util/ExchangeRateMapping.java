package com.ovh.milestone.Util;

import com.opencsv.CSVReader;
import com.ovh.milestone.Conversion.ForexRate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All currencies applied here are from EUR to USD The
 *
 *
 * Algorithm of the mapping is as follows :
 *
 * 1. Read the CSV file that contains the dates and FOREX rates 2. Check the quality of the CSV
 * file's data (date format & currency) 3. If the quality of the data is met, map the dates &
 * currencies as K, V
 *
 * Preferably, you might want to have a clean pre-processed CSV file, with dates formatted
 * like so : yyyy-MM-dd, sorted and without odd and special characters.
 *
 *
 */
public class ExchangeRateMapping {

    // This map will contain dates(Strings) as Key
    // and FOREX rates (Doubles) as Values
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateMapping.class.getName());
    public static final Map<String, Double> map = new TreeMap<>();




    public Map<String, Double> csvToMap(String csvFile) throws IOException {

        Double lastKnownXRate = null;
        File f = new File("forex.csv");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {

            // Read the CSV file
            try (CSVReader read = new CSVReader(new FileReader(csvFile), ',')) {
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

                                // Write to csv
                                String date = line[0];
                                Double forex = lastKnownXRate;
                                ForexRate fx = new ForexRate(date, forex);
                                bw.write(fx.getDate() + "," + fx.getForex());
                                bw.newLine();
                                bw.flush();

                            } else {

                                // If my K, V are ok, then I can put the info to the map
                                map.put(line[0], Double.parseDouble(line[1]));

                                // Add to temporary variable for off days
                                // Set off days to previous known date
                                lastKnownXRate = Double.parseDouble(line[1]);

                                // Write to csv
                                String date = line[0];
                                Double forex = lastKnownXRate;
                                ForexRate fx = new ForexRate(date, forex);
                                bw.write(fx.getDate() + "," + fx.getForex());
                                bw.newLine();
                                bw.flush();
                            }
                        }
                    }
                }
            }

            displayMap(map);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return map;
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
        if (s != null && s.trim().length() == 10 && "([0-9]{4})-([0-9]{2})-([0-9]{2})".matches(s)) {
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

        Pattern pattern = Pattern.compile("[$&+,:;=?@#|'<>.-^*()%!]");
        Matcher matcher = pattern.matcher("\\.[]{}()*+-?^$|");

        try {
            // Check that that the line's length is > 0
            if (s.trim().length() >= 1) {
                // if correct, return true
                return true;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return true;
    }
}
