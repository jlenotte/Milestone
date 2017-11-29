package com.ovh.milestone;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.java.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSource {


    private static final Logger LOGGER = LoggerFactory.getLogger(DataSource.class.getName());




    public List<Invoice> readFile(DataSet<String> fileName) {
        // Liste d'Invoice
        ArrayList<Invoice> list = new ArrayList<>();

        try {
            LOGGER.debug("Reading CSV file...");

            // Read with CSVReader from openCSV
            try (CSVReader reader = new CSVReader(new FileReader("dataBase3.csv"), ',')) {
                // String Array to format the pojo
                String[] nextLine;
                int index = 0;

                // As long as the file has a line ...
                while ((nextLine = reader.readNext()) != null) {
                    //                System.out.println(
                    //                    nextLine[0] + " " + nextLine[1] + " " + nextLine[2] + " " + nextLine[3] + " "
                    //                        + nextLine[4]);

                    String nic = nextLine[index++];
                    String name = nextLine[index++];
                    String firstName = nextLine[index++];
                    double transaction = Double.parseDouble(nextLine[index++]);
                    String currency = nextLine[index++];
                    ZonedDateTime date = ZonedDateTime.parse(nextLine[index++]);

                    Invoice c = new Invoice(nic, name, firstName, transaction, currency, date);
                    list.add(c);
                }
            }
            LOGGER.debug("File was read with success.");
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return list;
    }
}
