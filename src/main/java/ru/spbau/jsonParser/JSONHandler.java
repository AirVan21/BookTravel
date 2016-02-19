package ru.spbau.jsonParser;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

/**
 * Created by airvan21 on 15.02.16.
 */
public class JSONHandler {

    public static Map<String, List<String>> readFromLink(String urlToJSON) throws IOException {
        URL url = new URL(urlToJSON);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        Scanner reader = new Scanner(request.getInputStream());
        String resultLine = new String();
        while (reader.hasNext()) {
            resultLine += reader.nextLine();
        }
        reader.close();

        Gson gson = new Gson();
        Type gsonType = new TypeToken<Map<String, List<String>>>() {}.getType();

        return gson.fromJson(resultLine, gsonType);
    }

    public static Map<String, List<String>> readFromPath(String pathToJSON) throws IOException {
        String resultLine = new String(readAllBytes(get(pathToJSON)));

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap<String, ArrayList<String>>>() {}.getType();

        return gson.fromJson(resultLine, gsonType);
    }
}
