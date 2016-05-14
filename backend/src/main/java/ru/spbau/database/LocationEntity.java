package ru.spbau.database;


import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 16.03.16.
 */
public class LocationEntity {
    private static int QUOTE_THRESHOLD = 3;
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

    public void saveQuotesInDatabase(Datastore ds) {
        quotes
                .forEach(ds::save);
    }

    public void skipMeaninglessQuotes() {
        if (quotes.size() <= QUOTE_THRESHOLD) {
            return;
        }

        List<Quote> result = quotes
                .stream()
                .filter(Quote::isNotable)
                .collect(Collectors.toList());

        if (result.size() >= QUOTE_THRESHOLD) {
            return;
        }

        for (Quote quote : quotes) {
            if (result.size() >= QUOTE_THRESHOLD) {
                break;
            }

            if (!quote.isNotable()) {
                result.add(quote);
            }
        }

        setQuotes(result);
    }
}
