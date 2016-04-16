package ru.spbau.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by airvan21 on 16.03.16.
 */
public class LocationPair {
    public String cityName;
    public List<String> quotes = new ArrayList<>();

    public LocationPair() {}

    public LocationPair(String cityName, List<String> quotes) {
        this.cityName = cityName;
        this.quotes = quotes;
    }

    public LocationPair(String cityName, Set<String> quotes) {
        this.cityName = cityName;
        this.quotes.addAll(quotes);
    }
}
