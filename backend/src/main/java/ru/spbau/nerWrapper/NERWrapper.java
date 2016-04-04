package ru.spbau.nerWrapper;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import ru.spbau.database.LocationPair;
import ru.spbau.decisions.LengthJudge;
import ru.spbau.decisions.SentimentJudge;
import ru.spbau.locationRecord.LocationRecord;

import java.util.*;

/**
 * Named Entity Recognizer Wrapper
 */
public class NERWrapper {
    private final AbstractSequenceClassifier<CoreLabel> classifier;
    private final static String searchTag = "LOCATION";

    public NERWrapper (AbstractSequenceClassifier<CoreLabel> serializedClassifier)  {
        classifier = serializedClassifier;
    }

    public List<LocationPair> classifyBook(String pathToBook) {
        final List<List<CoreLabel>> taggedText = classifier.classify(pathToBook);
        final Map<String, Set<String>> locationMap = new HashMap<>();
        final List<LocationPair> locationList = new ArrayList<>();

        taggedText.forEach(sentence -> handleLocation(sentence, locationMap));
        // TODO: rewrite
        locationMap.entrySet().forEach(item -> locationList.add(new LocationPair(item.getKey(), item.getValue())));
        return locationList;
    }

    /**
     * Retrieves if sentence has geo-location tag.
     * Multi-word get-locations will be merged in one string.
     * (for e.g. San Francisco)
     *
     * @param sentence from epub-book
     * @param locationMap location retrieved from sentence
     */
    private void handleLocation(List<CoreLabel> sentence, Map<String, Set<String>> locationMap) {
        Iterator<CoreLabel> sentenceIterator = sentence.iterator();
        LengthJudge judge = new LengthJudge();

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

                String sentenceLine = Sentence.listToOriginalTextString(sentence);
                if (judge.makeDecision(sentenceLine)) {
                    locationMap.putIfAbsent(locationName, new HashSet<>());
                    locationMap.get(locationName).add(sentenceLine);
                }
            }
        }
    }

    // draft
    private void logSemantics(List<LocationPair> locations) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

        SentimentJudge judge = new SentimentJudge(props);
        for (LocationPair item : locations) {
            for (String line : item.quotes) {
                if (judge.makeDecision(line)) {
                    System.out.print(" OK ");
                } else {
                    System.out.print(" NOK ");
                }
                System.out.println(line);
            }
        }
    }
}
