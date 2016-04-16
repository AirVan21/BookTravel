package ru.spbau.books.decisions;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

/**
 * Created by airvan21 on 04.04.16.
 */
public class SentimentJudge implements Judge {
    private final StanfordCoreNLP pipeline;
    private enum SentimentGrade {
        VERY_NEGATIVE
        , NEGATIVE
        , NEUTRAL
        , POSITIVE
        , VERY_POSITIVE
    }

    public SentimentJudge(Properties properties) {
        pipeline = new StanfordCoreNLP(properties);
    }

    public SentimentJudge() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    /**
     * Verifies if sentence has a sentimental aspect
     *
     * @param sentence sentence from the book
     * @return true  - if sentence is positive or negative
     *         false - if sentence is neutral
     */
    @Override
    public boolean makeDecision(String sentence) {
        int score = getScore(sentence);

        return !(SentimentGrade.NEUTRAL.ordinal() == score);
    }

    /**
     * Sentiment score grades:
     *  0 - Very Negative
     *  1 - Negative
     *  2 - Neutral
     *  3 - Positive
     *  4 - Very Positive
     */
    public int getScore(String sentence) {
        int score = 0;
        // TODO: check score calculation
        Annotation annotation = pipeline.process(sentence);
        for (CoreMap quote :  annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = quote.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            score = RNNCoreAnnotations.getPredictedClass(tree);
        }

        return score;
    }
}
