package ru.spbau.books.statistics;

import ru.spbau.books.decisions.StanfordSentimentJudge;
import ru.spbau.database.BookRecord;
import ru.spbau.database.LocationEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by airvan21 on 04.04.16.
 */
public class BookStatistics {
    private int numberOfBooks = 0;
    private int numberOfSentences = 0;
    private List<Integer> numberOfCharsInSentences = new ArrayList<>();
    private List<Integer> sentimentScore           = new ArrayList<>();
    private final StanfordSentimentJudge judge     = new StanfordSentimentJudge();

    public BookStatistics() {}

    public BookStatistics(int numberOfBooks, List<Integer> numberOfCharsInSentences, int numberOfSentences) {
        this.numberOfBooks = numberOfBooks;
        this.numberOfCharsInSentences = numberOfCharsInSentences;
        this.numberOfSentences = numberOfSentences;
    }

    public void addBookStatistics(BookRecord book) {
        numberOfBooks++;
        for (LocationEntity item : book.cities) {
            numberOfSentences += item.getQuotes().size();
            item.getQuotes().forEach(quote -> numberOfCharsInSentences.add(quote.getSource().length()));
        }
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    public List<Integer> getNumberOfCharsInSentences() {
        return numberOfCharsInSentences;
    }

    public void setNumberOfCharsInSentences(List<Integer> numberOfCharsInSentences) {
        this.numberOfCharsInSentences = numberOfCharsInSentences;
    }

    public int getNumberOfSentences() {
        return numberOfSentences;
    }

    public void setNumberOfSentences(int numberOfSentences) {
        this.numberOfSentences = numberOfSentences;
    }

    public List<Integer> getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(List<Integer> sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}
