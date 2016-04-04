package ru.spbau.nerWrapper;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import ru.spbau.locationRecord.LocationRecord;

import java.util.*;

/**
 * Named Entity Recognizer Wrapper
 */
public class NERWrapper {
    private AbstractSequenceClassifier<CoreLabel> classifier;
    private final static String searchTag = "LOCATION";

    public NERWrapper (AbstractSequenceClassifier<CoreLabel> serializedClassifier)  {
        classifier = serializedClassifier;
    }

    public List<LocationRecord> classifyBook(String pathToBook) {
        final List<List<CoreLabel>> taggedText = classifier.classify(pathToBook);
        final List<LocationRecord> locationList = new ArrayList<>();

        taggedText.forEach(sentence -> handleLocation(sentence, locationList));

        return locationList;
    }

    /**
     * Retrieves if sentence has geo-location tag.
     * Multi-word get-locations will be merged in one string.
     *
     * Question: What should we do with "Fairbanks, Alaska"?
     *
     * @param sentence from epub-book
     * @param locationList location retrieved from sentence
     */
    private void handleLocation(List<CoreLabel> sentence, List<LocationRecord> locationList) {
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
                locationList.add(new LocationRecord(locationName, sentence));
            }
        }
    }
}
