package ru.spbau.database;

import edu.stanford.nlp.pipeline.Annotation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import ru.spbau.books.annotator.RelationExtractor;
import ru.spbau.books.decisions.SentimentGrade;
import ru.spbau.books.decisions.SentimentJudge;
import ru.spbau.books.decisions.StanfordSentimentJudge;


/**
 * Created by airvan21 on 22.04.16.
 */
@Entity
public class Quote {
    @Id
    private ObjectId id;
    private final String source;
    private final String cityName;
    private boolean isNotable = false;
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

    public boolean isNotable() {
        return isNotable;
    }

    public void setIsNotable(boolean isNotable) {
        this.isNotable = isNotable;
    }

    public void modifySentiment(SentimentJudge sentimentJudge) {
        sentiment = sentimentJudge.getSentimentScore(source).toString();
    }

    public void modifySentiment(Annotation annotation) {
        sentiment = StanfordSentimentJudge.getSentimentScore(annotation).toString();
    }

    public void modifyIsNotable(Annotation annotation) {
        isNotable = RelationExtractor.extractMainRelations(annotation).contains(cityName);
    }

    @Override
    public String toString() {
        return source + "\n" + sentiment;
    }
}
