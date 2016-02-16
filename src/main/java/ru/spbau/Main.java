package ru.spbau;

import ru.spbau.jsonParser.DataHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String url = "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";
        String path = "./data/countriesToCitiesFile.json";

        try {
            final String russia = "Russia";
            Map<String, List<String>> jsonData = DataHandler.handlePath(path);
            List<String> towns = jsonData.get(russia);
            System.out.println(towns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
