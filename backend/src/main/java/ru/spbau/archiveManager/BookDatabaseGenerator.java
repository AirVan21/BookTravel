package ru.spbau.archiveManager;

import com.google.api.services.books.Books;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import nl.siegmann.epublib.domain.Metadata;
import org.mongodb.morphia.Datastore;
import ru.spbau.books.decisions.SentimentJudge;
import ru.spbau.books.decisions.StanfordSentimentJudge;
import ru.spbau.books.processor.BookProcessor;
import ru.spbau.database.*;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.googleAPI.BookSearcher;
import ru.spbau.locationRecord.LocationValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 23.02.16.
 */
public class BookDatabaseGenerator {

    static public void generateBookDataBase(String pathToIndexFile,  AbstractSequenceClassifier<CoreLabel> classifier,
                                            Datastore ds, Datastore validate) {
        try {
            List<String> pathsToBooks = Files.lines(Paths.get(pathToIndexFile))
                    .collect(Collectors.toList());

            LocationValidator validator = new LocationValidator(validate);

            Books bookManager = null;
            try {
                 bookManager = BookSearcher.generateBooksManager(BookSearcher.APPLICATION_NAME,
                         BookSearcher.GOOGLE_API_CODE);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                System.out.println("Book Searcher creation went wrong!");
            }

            for (String pathToBook : pathsToBooks) {
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);
                if (!EPUBHandler.isEPUBValid(bookMetadata)) {
                    continue;
                }

                List<LocationEntity> locationList = processBook(pathToBook, classifier, validator);

                if (!locationList.isEmpty()) {
                    BookRecord bookRecord = new BookRecord(bookMetadata, locationList);
                    if (bookRecord.description.isEmpty()) {
                        bookRecord.setDescriptionFromBooksAPI(bookManager);
                    }
                    System.out.println(bookRecord);
                    bookRecord.saveInDatabase(ds);
                }
            }
        } catch (IOException e1) {}
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
