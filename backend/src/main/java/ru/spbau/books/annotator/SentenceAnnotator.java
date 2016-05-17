package ru.spbau.books.annotator;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * Created by airvan21 on 04.05.16.
 */
public class SentenceAnnotator {
    private final StanfordCoreNLP pipeline;

    public SentenceAnnotator(Properties properties) {
        pipeline = new StanfordCoreNLP(properties);
    }

    public SentenceAnnotator() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment, lemma, natlog, openie");
        pipeline = new StanfordCoreNLP(props);
    }

    public Annotation annotate(String sentence) {
        return pipeline.process(sentence);
    }
}
