package ru.spbau;

import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.jsonParser.JSONHandler;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        String url = "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";
        String jsonPath = "./data/countriesToCitiesFile.json";
        String bookPath = "./data/BratstvoKoljca.epub";

        Map<String, List<String>> jsonData = JSONHandler.readFromPath(jsonPath);
        for (String countryName : jsonData.keySet()) {
            System.out.println(countryName + ": " + jsonData.get(countryName).size());
        }

        System.out.println(EPUBHandler.readFromPath(bookPath));
    }
}
