package ru.spbau.books.decisions;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
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
//        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment, lemma, depparse, natlog, openie");
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
     * Sentiment score grades according to Stanford spec:
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

//            System.out.println("Sentence #" + sentence + ": " + quote.get(CoreAnnotations.TextAnnotation.class));
//            // Print SemanticGraph
//            System.out.println(quote.get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));

            Tree tree = quote.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            score = RNNCoreAnnotations.getPredictedClass(tree);
        }

        SentimentGrade result = SentimentGrade.NEUTRAL;

        // Grouping results with VERY
        if (score > 2) {
            result = SentimentGrade.POSITIVE;
        }

        if (score < 2) {
            result = SentimentGrade.NEGATIVE;
        }

        return result;
    }
}
