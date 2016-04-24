package ru.spbau.database;

import ru.spbau.books.decisions.SentimentGrade;
import ru.spbau.books.decisions.SentimentJudge;

/**
 * Created by airvan21 on 22.04.16.
 */
public class Quote {
    private final String source;
    private final String cityName;

    private int rating = 0;

    private String sentiment = SentimentGrade.NEUTRAL.toString();
    public Quote(String source, String cityName) {
        this.source = source;
        this.cityName = cityName;
    }

    public Quote(String source, String cityName, String sentiment, int rating) {
        this.source = source;
        this.sentiment = sentiment;
        this.cityName = cityName;
        this.rating = rating;
    }

    public String getCityName() {
        return cityName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getSource() {
        return source;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void modifySentiment(SentimentJudge sentimentJudge) {
        sentiment = sentimentJudge.getSentimentScore(source).toString();
    }

    @Override
    public String toString() {
        return source + "\n" + sentiment;
    }
}
