package ru.spbau.archiveManager;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import nl.siegmann.epublib.domain.Metadata;
import org.mongodb.morphia.Datastore;
import ru.spbau.books.decisions.SentimentJudge;
import ru.spbau.books.decisions.StanfordSentimentJudge;
import ru.spbau.books.decisions.WatsonSentimentJudge;
import ru.spbau.books.processor.BookProcessor;
import ru.spbau.csvHandler.CityEntry;
import ru.spbau.database.*;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.locationRecord.LocationValidator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 23.02.16.
 */
public class BookDatabaseGenerator {

    static public void generateBookDataBase(String pathToIndexFile,  AbstractSequenceClassifier<CoreLabel> classifier,
                                            Datastore ds, Datastore validate) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile))) {
            LocationValidator validator = new LocationValidator(validate);

            for (String pathToBook; (pathToBook = reader.readLine()) != null;) {
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);

                if (!bookMetadata.getLanguage().equals("en")) {
                    continue;
                }

                if (bookMetadata.getAuthors().size() == 0 || bookMetadata.getAuthors().get(0).getFirstname().equals("")) {
                    continue;
                }

                List<LocationEntity> locationList = processBook(pathToBook, classifier, validator);
                if (!locationList.isEmpty()) {
                    BookRecord bookRecord = new BookRecord(bookMetadata, locationList);
                    System.out.println(bookRecord);
//                    ds.save(bookRecord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and analyses book content.
     */
    static private List<LocationEntity> processBook(String pathToBook, AbstractSequenceClassifier<CoreLabel> classifier,
                                                  LocationValidator validator) throws IOException {

        BookProcessor processor = new BookProcessor(classifier);
        List<LocationEntity> locationList = processor.processBook(pathToBook)
                .stream()
                .filter(location -> validator.validateWithDB(location.getCityName()))
                .collect(Collectors.toList());

        // Run sentiment analysis on sentences
        final SentimentJudge judge = new StanfordSentimentJudge();
        locationList
                .forEach(location -> location
                        .getQuotes()
                        .stream()
                        .forEach(quote -> quote.modifySentiment(judge)));

        return locationList;
    }
}
