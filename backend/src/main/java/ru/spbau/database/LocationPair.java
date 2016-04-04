package ru.spbau.database;

import java.util.List;

/**
 * Created by airvan21 on 16.03.16.
 */
public class LocationPair {
    public String cityName;
    public List<String> quotes;

    public LocationPair() {}

    public LocationPair(String cityName, List<String> quotes) {
        this.cityName = cityName;
        this.quotes = quotes;
    }
}
