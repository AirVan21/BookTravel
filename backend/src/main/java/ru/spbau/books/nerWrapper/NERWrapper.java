package ru.spbau.books.nerWrapper;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.util.Triple;
import ru.spbau.database.LocationPair;
import ru.spbau.books.decisions.LengthJudge;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Named Entity Recognizer Wrapper
 */
public class NERWrapper {
    private final AbstractSequenceClassifier<CoreLabel> classifier;
    private final static String searchTag = "LOCATION";

    public NERWrapper(AbstractSequenceClassifier<CoreLabel> serializedClassifier)  {
        classifier = serializedClassifier;
    }

    public List<LocationPair> classifyBook(String book) {
        final List<List<CoreLabel>> taggedText = classifier.classify(book);
        final Map<String, Set<String>> locationMap = new HashMap<>();

        taggedText.forEach(sentence -> handleLocation(sentence, locationMap));

        return locationMap.entrySet().stream()
                .map(NERWrapper::convert)
                .collect(Collectors.toList());
    }

    private static LocationPair convert(Map.Entry<String, Set<String>> entry ) {
        return new LocationPair(entry.getKey(), entry.getValue());
    }

    // Maybe
    public void classifySentence(List<CoreLabel> sentence, String line) {
        final List<CoreLabel> taggedText = classifier.classifySentence(sentence);
        List<Triple<String,Integer,Integer>> test = classifier.classifyToCharacterOffsets(line);
        if (!test.isEmpty()) {
            System.out.println(test);
        }
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

                String sentenceLine = Sentence
                        .listToOriginalTextString(sentence)
                        .replaceAll("\\s+(?=[),.!?;:'])", "");

                if (judge.makeDecision(sentenceLine)) {
                    locationMap.putIfAbsent(locationName, new HashSet<>());
                    locationMap.get(locationName).add(sentenceLine);
                }
            }
        }
    }
}
