package ru.spbau.books.decisions;

/**
 * Created by airvan21 on 04.04.16.
 */
public class LengthJudge implements Judge {
    public final static int minimalLength = 50;
    public final static int maximalLength = 400;

    @Override
    public boolean makeDecision(String sentence) {
        return sentence.length() <= maximalLength && sentence.length() >= minimalLength;
    }
}
