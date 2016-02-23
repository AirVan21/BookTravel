package ru.spbau.nerWrapper;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Named Entity Recognizer Wrapper
 */
public class NERWrapper {
    private AbstractSequenceClassifier<CoreLabel> classifier;
    private final static String searchTag = "LOCATION";

    /**
     * Exception throwing constructor. Rewrite?
     */
    public NERWrapper (String pathToClassifier) throws IOException, ClassNotFoundException {
        classifier = CRFClassifier.getClassifier(pathToClassifier);
    }

    public Map<String, List<CoreLabel>> classifyBook(String pathToBook) {
        List<List<CoreLabel>> taggedText = classifier.classify(pathToBook);
        Map<String, List<CoreLabel>> locationMap = new HashMap<String, List<CoreLabel>>();

        for (List<CoreLabel> sentence : taggedText) {
            handleLocation(sentence, locationMap);
        }

        return locationMap;
    }

    /**
     * Retrieves if sentence has geo-location tag.
     * Multi-word get-locations will be merged in one string.
     *
     * Question: What should we do with "Fairbanks, Alaska"?
     *
     * @param sentence
     * @param locationMap
     */
    private void handleLocation(List<CoreLabel> sentence, Map<String, List<CoreLabel>> locationMap) {
        Iterator<CoreLabel> sentenceIterator = sentence.iterator();

        while (sentenceIterator.hasNext()) {
            CoreLabel item = sentenceIterator.next();

            if (item.get(CoreAnnotations.AnswerAnnotation.class).equals(searchTag)) {
                String locationName = item.value();
                // Handle multi-word location
                while (sentenceIterator.hasNext()) {
                    item = sentenceIterator.next();

                    if (item.get(CoreAnnotations.AnswerAnnotation.class).equals(searchTag)) {
                        locationName += " " + item.value();
                    } else {
                        break; // Geo-location ended
                    }
                }
                locationMap.put(locationName, sentence);
            }
        }
    }
}
