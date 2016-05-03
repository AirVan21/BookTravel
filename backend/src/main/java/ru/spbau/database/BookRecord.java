package ru.spbau.database;

import com.google.api.services.books.Books;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Metadata;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.googleAPI.BookSearcher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 14.03.16.
 */
@Entity("Book")
public class BookRecord {
    @Id
    public ObjectId bookID;
    public String title;
    public String language;
    public String description;
    public List<BookAuthor> authors;
    public List<LocationEntity> cities;

    public BookRecord() {}

    public BookRecord(Metadata metadata, List<LocationEntity> locationsFromBook) {
        title = metadata.getFirstTitle();
        language = metadata.getLanguage();
        authors = convertAuthorsList(metadata.getAuthors());
        cities = locationsFromBook;
        setDescriptionFromMetadata(metadata);
    }

    public void saveInDatabase(Datastore ds) {
        cities
                .forEach(city -> city.saveQuotesInDatabese(ds));
        ds.save(this);
    }

    public void setDescriptionFromBooksAPI(Books bookManager) {
        BookSearcher bookSearcher = new BookSearcher(bookManager);
        Optional<String> result = bookSearcher.getBookDescription(title, authors);
        if (result.isPresent()) {
            description = result.get();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================================\n");

        sb.append(title + "\n");
        sb.append(language + "\n");

        sb.append("-----------------------------------------------------------------\n");
        for (LocationEntity location : cities) {
            sb.append(authors + "\n");
            sb.append("--------------\n");
            sb.append(location.getCityName() + "\n");
            sb.append("--------------\n");
            location.getQuotes().forEach(sentence -> sb.append(sentence + "\n"));
        }
        sb.append("================================================================\n\n");

        return sb.toString();
    }

    private void setDescriptionFromMetadata(Metadata metadata) {
        String bookDescription = "";
        if (!metadata.getDescriptions().isEmpty()) {
            bookDescription = metadata.getDescriptions().get(0);
        }

        description = bookDescription;
    }

    private List<BookAuthor> convertAuthorsList(List<Author> authors) {
        return authors
                .stream()
                .map(BookAuthor::new)
                .collect(Collectors.toList());
    }
}
