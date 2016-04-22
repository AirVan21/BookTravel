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
    private Gson gson;

    public JSONHandler() {
        gson = new Gson();
    }

    public  Map<String, List<String>> readFromLink(String urlToJSON) throws IOException {
        URL url = new URL(urlToJSON);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        Scanner reader = new Scanner(request.getInputStream());
        StringBuilder sb = new StringBuilder();
        while (reader.hasNext()) {
            sb.append(reader.nextLine());
        }
        reader.close();

        Type gsonType = new TypeToken<Map<String, List<String>>>() {}.getType();

        return gson.fromJson(sb.toString(), gsonType);
    }

    public  Map<String, List<String>> readFromPath(String pathToJSON) throws IOException {
        String resultLine = new String(readAllBytes(get(pathToJSON)));
        Type gsonType = new TypeToken<HashMap<String, ArrayList<String>>>() {}.getType();

        return gson.fromJson(resultLine, gsonType);
    }
}
