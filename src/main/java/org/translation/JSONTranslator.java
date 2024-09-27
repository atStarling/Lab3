package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private List<String> countries = new ArrayList<>();
    private JSONArray jsonArray = new JSONArray();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            this.jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                countries.add(jsonArray.getJSONObject(i).getString("alpha3").toUpperCase());
            }

            // TODO Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        List<String> keys = new ArrayList<>();
        int index = countries.indexOf(country.toUpperCase());
        JSONObject gamma = jsonArray.getJSONObject(index);
        int index2 = 0;
        // I love bodging.

        for (String key : gamma.keySet()) {
            if (!Set.of("alpha3", "alpha2", "id").contains(key)) {
                keys.add(key);
            }
            index2++;
        }

        return keys;
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        country = country.toUpperCase();

        if (countries.contains(country)) {
            List<String> special = getCountryLanguages(country);
            if (special.contains(language)) {
                int index = countries.indexOf(country);
                System.out.println(index);
                return jsonArray.getJSONObject(index).getString(language);
            }
        }

        return null;
    }
}
