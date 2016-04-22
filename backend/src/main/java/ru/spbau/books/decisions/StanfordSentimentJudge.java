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
public class StanfordSentimentJudge implements SentimentJudge {
    private final StanfordCoreNLP pipeline;

    public StanfordSentimentJudge(Properties properties) {
        pipeline = new StanfordCoreNLP(properties);
    }

    public StanfordSentimentJudge() {
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
    public boolean shouldAccept(String sentence) {
        return !(SentimentGrade.NEUTRAL.equals(getSentimentScore(sentence)));
    }

    /**
     * Sentiment score grades:
     *  0 - Very Negative
     *  1 - Negative
     *  2 - Neutral
     *  3 - Positive
     *  4 - Very Positive
     */
    @Override
    public SentimentGrade getSentimentScore(String sentence) {
        int score = 0;

        Annotation annotation = pipeline.process(sentence);
        for (CoreMap quote :  annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = quote.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            score = RNNCoreAnnotations.getPredictedClass(tree);
        }

        return SentimentGrade.values()[score];
    }
}
