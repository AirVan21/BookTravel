package ru.spbau.database;

import ru.spbau.books.decisions.SentimentGrade;
import ru.spbau.books.decisions.SentimentJudge;

/**
 * Created by airvan21 on 22.04.16.
 */
public class Quote {
    private final String source;
    private String sentiment = SentimentGrade.NEUTRAL.toString();

    public Quote(String source, SentimentJudge sentimentJudge) {
        this.source = source;
        this.sentiment = sentimentJudge
                .getSentimentScore(source)
                .toString();
    }

    public Quote(String sentiment, String source) {
        this.sentiment = sentiment;
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public String getSentiment() {
        return sentiment;
    }
}
