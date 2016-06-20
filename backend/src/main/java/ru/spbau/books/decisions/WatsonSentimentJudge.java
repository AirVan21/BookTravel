package ru.spbau.books.decisions;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by airvan21 on 15.04.16.
 */
public class WatsonSentimentJudge implements SentimentJudge {
    private static final AlchemyLanguage SERVICE = new AlchemyLanguage();
    private static final String API_KEY = "";

    public WatsonSentimentJudge() {
        SERVICE.setApiKey(API_KEY);
    }

    @Override
    public boolean shouldAccept(String sentence) {
        return !getSentimentScore(sentence).equals(SentimentGrade.NEUTRAL);
    }

    @Override
    public SentimentGrade getSentimentScore(String quote) {
        final Map<String, Object> params = new HashMap<>();
        params.put(AlchemyLanguage.TEXT, quote);
        final String sentiment = SERVICE
                .getSentiment(params)
                .getSentiment()
                .getType()
                .toString();

        return SentimentGrade.valueOf(sentiment);
    }
}
