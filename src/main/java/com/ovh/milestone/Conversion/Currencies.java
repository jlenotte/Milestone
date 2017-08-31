package com.ovh.milestone.Conversion;

import static java.lang.Double.parseDouble;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Map Date & Double
 */
public class Currencies
{

    private static final Logger LOG = LoggerFactory.getLogger(Currencies.class.getName());



    public void csvToMap() throws IOException
    {
        List<String> dates = new ArrayList<>();
        List<Double> sums = new ArrayList<>();

        Map<String, Double> map = new HashMap<>();

        // ouvrir le fichier csv
        try (CSVReader read = new CSVReader(new FileReader("/home/jlenotte/WORKSPACE/Milestone/dateLines.csv"), ','))
        {
            String[] line;
            // lire chaque ligne
            // verifier que la ligne est non vide
            while ((line = read.readNext()) != null)
            {
                // verifier que l'on a deux elements bien splittes
                if (line.length == 2)
                {
                    // verifier que l'on a une clef qui correspond à une date
                    // verifier que l'on a une valeur qui correspond à un taux de change
                    if (line[0] != null && isVraiDate(line[0]) &&
                        line[1] != null && isVraiTauxChange(line[1]))
                    {
                        // si ma clef et ma valeur sont ok
                        // alors je mets peux stocker cette info
                        map.put(line[0], Double.parseDouble(line[1]));
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }

        try (CSVReader read = new CSVReader(new FileReader("/home/jlenotte/WORKSPACE/Milestone/dateLines.csv"), ','))
        {
            String[] line;
            while ((line = read.readNext()) != null)
            {
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



    /**
     *
     * @param s
     * @return
     */
    private boolean isVraiTauxChange(String s)
    {

        // "0.0"
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



    private boolean isVraiDate(String s)
    {
        return true;
    }
}
