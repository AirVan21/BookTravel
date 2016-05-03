package ru.spbau.database;


import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 16.03.16.
 */
public class LocationEntity {
    private String cityName;
    @Reference
    private List<Quote> quotes = new ArrayList<>();

    public LocationEntity() {}

    public LocationEntity(String cityName, List<String> quotes) {
        this.cityName = cityName;
        this.quotes = quotes
                .stream()
                .map(quote -> new Quote(quote, cityName))
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

    public void saveQuotesInDatabese(Datastore ds) {
        quotes
                .forEach(item -> ds.save(item));
    }
}
