package ru.spbau.decisions;

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
        VeryNegative
        , Negative
        , Neutral
        , Positive
        , VeryPositive
    }

    public SentimentJudge(Properties properties) {
        pipeline = new StanfordCoreNLP(properties);
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

        return !(SentimentGrade.Neutral.ordinal() == score);
    }

    /**
     * Sentiment score grades:
     *  0 - Very Negative
     *  1 - Negative
     *  2 - Neutral
     *  3 - Positive
     *  4 - Very Positive
     */
    private int getScore(String sentence) {
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

        int score = 0;
        Annotation annotation = pipeline.process(sentence);
        for (CoreMap quote :  annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = quote.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentimental = RNNCoreAnnotations.getPredictedClass(tree);
            score = Math.max(sentimental, score);
            System.out.print(quote.get(SentimentCoreAnnotations.SentimentClass.class));
        }

        return score;
    }
}
