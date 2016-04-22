package ru.spbau.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 16.03.16.
 */
public class LocationEntity {
    private String cityName;
    private List<Quote> quotes = new ArrayList<>();

    public LocationEntity() {}

    public LocationEntity(String cityName, List<String> quotes) {
        this.cityName = cityName;
        this.quotes = quotes
                .stream()
                .map(Quote::new)
                .collect(Collectors.toList());
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
}
