package ru.spbau.archiveManager;

import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import nl.siegmann.epublib.domain.Metadata;
import org.mongodb.morphia.Datastore;
import ru.spbau.books.annotator.SentenceAnnotator;
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
            List<String> pathsToBooks = Files
                    .lines(Paths.get(pathToIndexFile))
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

            BookSearcher bookSearcher = new BookSearcher(bookManager);

            for (String pathToBook : pathsToBooks) {
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);
                if (!EPUBHandler.isEPUBValid(bookMetadata)) {
                    continue;
                }

                List<LocationEntity> locationList = processBook(pathToBook, classifier, validator);

                if (!locationList.isEmpty()) {
                    BookRecord bookRecord = new BookRecord(bookMetadata, locationList);
                    // TODO: move upper in a code block + rewrite on a metadata parameter
                    Optional<Volumes> dataFromGoogleBooks = bookSearcher.performQuery(bookRecord.getTitle(), bookRecord.getAuthors());

                    if (!dataFromGoogleBooks.isPresent()) {
                        continue;
                    }

                    if (bookRecord.setDescriptionFromBooksAPI(dataFromGoogleBooks.get()) &&
                            bookRecord.setCoverLinkFromBooksAPI(dataFromGoogleBooks.get())) {
                        System.out.println(bookRecord);
//                        bookRecord.saveInDatabase(ds);
                    }
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

        final SentenceAnnotator annotator = new SentenceAnnotator();

        locationList
                .forEach(location -> location
                .getQuotes()
                .stream()
                .forEach(quote -> {
                    final Annotation annotation = annotator.annotate(quote.getSource());
                    quote.modifySentiment(annotation);
                    quote.modifyIsNotable(annotation);
                }));

        return locationList;
    }
}
