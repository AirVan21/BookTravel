package ru.spbau.books.decisions;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by airvan21 on 15.04.16.
 */
public class WatsonSentimentJudge implements SentimentJudge {
    static final AlchemyLanguage service = new AlchemyLanguage();
    static final String apiKey = "9b45f03b9b6d914419840a5f3c7ca911c3f33ae5";

    public WatsonSentimentJudge() {
        service.setApiKey(apiKey);
    }

    @Override
    public boolean shouldAccept(String sentence) {
        return !getSentimentScore(sentence).equals(SentimentGrade.NEUTRAL);
    }

    @Override
    public SentimentGrade getSentimentScore(String quote) {
        final Map<String, Object> params = new HashMap<>();
        params.put(AlchemyLanguage.TEXT, quote);
        final String sentiment = service
                .getSentiment(params)
                .getSentiment()
                .getType()
                .toString();

        return SentimentGrade.valueOf(sentiment);
    }
}
