package ru.spbau.books.processor;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by airvan21 on 09.04.16.
 */
public class LocationRecognizer {
    private final static String searchTag = "LOCATION";
    private final AbstractSequenceClassifier<CoreLabel> classifier;

    public LocationRecognizer(AbstractSequenceClassifier<CoreLabel> classifier) {
        this.classifier = classifier;
    }

    public Stream<String> classifySentence(String sentence) {
        final List<Triple<String, Integer, Integer>> tagsFromSentence = classifier.classifyToCharacterOffsets(sentence);

        return tagsFromSentence.stream()
                .filter(description -> description.first.equals(searchTag))
                .map(item -> sentence.substring(item.second, item.third))
                .map(String::trim)
                .distinct();
    }
}
