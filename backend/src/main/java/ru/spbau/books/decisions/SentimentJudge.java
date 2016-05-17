package ru.spbau.books.decisions;

/**
 * Created by airvan21 on 22.04.16.
 */
public interface SentimentJudge extends Judge {
    SentimentGrade getSentimentScore(String quote);
}
