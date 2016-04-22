package ru.spbau.books.processor;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordToSentenceProcessor;
import ru.spbau.books.decisions.LengthJudge;
import ru.spbau.database.LocationEntity;
import ru.spbau.epubParser.EPUBHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Created by airvan21 on 09.04.16.
 */
public class BookProcessor {
    private final LocationRecognizer recognizer;

    public BookProcessor(AbstractSequenceClassifier<CoreLabel> classifier) {
        recognizer = new LocationRecognizer(classifier);
    }

    public List<LocationEntity> processBook(String pathToBook) throws IOException {
        final String bookText = EPUBHandler.readFromPath(pathToBook);

        final LengthJudge lengthJudge = new LengthJudge();
        final Stream<String> sentences = tokenizeBook(bookText)
                .filter(lengthJudge::shouldAccept);

        return sentences
                .flatMap(sentence -> locationEntityStream(sentence, recognizer.classifySentence(sentence)))
                .collect(groupingBy(BookProcessor.LocationPair::getLocation, mapping(BookProcessor.LocationPair::getSentence, toList())))
                .entrySet()
                .stream()
                .map(entity -> new LocationEntity(entity.getKey(), entity.getValue()))
                .collect(toList());
    }

    private Stream<String> tokenizeBook(String bookText) {
        final List<CoreLabel> tokens = new ArrayList<>();
        final PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<>(new StringReader(bookText), new CoreLabelTokenFactory(), "");

        while (tokenizer.hasNext()) {
            tokens.add(tokenizer.next());
        }
        // Split sentences from tokens
        final List<List<CoreLabel>> sentences = new WordToSentenceProcessor<CoreLabel>().process(tokens);

        return extractOriginalText(bookText, sentences);
    }

    private Stream<String> extractOriginalText(String bookText, List<List<CoreLabel>> sentences) {
        return sentences
                .stream()
                .map(item -> extractOriginalSentence(bookText, item));
    }

    private String extractOriginalSentence(String bookText, List<CoreLabel> sentence) {
        final int firstWordIndex = 0;
        final int lastWordIndex  = sentence.size() - 1;

        final int start = sentence.get(firstWordIndex).beginPosition();
        final int end   = sentence.get(lastWordIndex).endPosition();

        return bookText.substring(start, end).trim();
    }

    private Stream<LocationPair> locationEntityStream(String sentence, Stream<String> locations) {
        return locations
                .map(location -> new LocationPair(location, sentence));
    }

    private static class LocationPair {
        private final String location;
        private final String sentence;

        public LocationPair(String location, String sentence) {
            this.location = location;
            this.sentence = sentence;
        }

        public String getLocation() {
            return location;
        }

        public String getSentence() {
            return sentence;
        }
    }
}
