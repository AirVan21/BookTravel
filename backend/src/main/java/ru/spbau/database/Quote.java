package ru.spbau.database;

import ru.spbau.books.decisions.SentimentGrade;
import ru.spbau.books.decisions.SentimentJudge;

/**
 * Created by airvan21 on 22.04.16.
 */
public class Quote {
    private final String source;
    private String sentiment = SentimentGrade.NEUTRAL.toString();

    public Quote(String source) {
        this.source = source;
    }

    public Quote(String source, String sentiment) {
        this.source = source;
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
