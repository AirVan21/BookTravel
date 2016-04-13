package ru.spbau.books.statistics;

import ru.spbau.database.BookRecord;
import ru.spbau.database.LocationPair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by airvan21 on 04.04.16.
 */
public class BookStatistics {
    private int numberOfBooks = 0;
    private int numberOfSentences = 0;
    private List<Integer> numberOfCharsInSentences = new ArrayList<>();

    public BookStatistics() {}

    public BookStatistics(int numberOfBooks, List<Integer> numberOfCharsInSentences, int numberOfSentences) {
        this.numberOfBooks = numberOfBooks;
        this.numberOfCharsInSentences = numberOfCharsInSentences;
        this.numberOfSentences = numberOfSentences;
    }

    public void addBookStatistics(BookRecord book) {
        numberOfBooks++;
        for (LocationPair item : book.cities) {
            numberOfSentences += item.quotes.size();
            item.quotes.forEach(quote -> numberOfCharsInSentences.add(quote.length()));
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
}
