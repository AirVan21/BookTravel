package ru.spbau.database;

import com.google.api.services.books.model.Volumes;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Metadata;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;
import ru.spbau.googleAPI.BookSearcher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 14.03.16.
 */
@Entity("Book")
public class BookRecord {
    @Id
    private ObjectId bookID;
    private String title;
    private String language;
    private String description = "";
    private String coverLink   = "";
    private List<BookAuthor> authors;
    private List<LocationEntity> cities;

    public BookRecord() {}

    public BookRecord(Metadata metadata, List<LocationEntity> locationsFromBook) {
        title = metadata.getFirstTitle();
        language = metadata.getLanguage();
        authors = convertAuthorsList(metadata.getAuthors());
        cities = locationsFromBook;
    }

    public void saveInDatabase(Datastore ds) {
        cities
                .forEach(city -> city.saveQuotesInDatabase(ds));
        ds.save(this);
    }

    public boolean setDescriptionFromBooksAPI(Volumes volumes) {
        Optional<String> result = BookSearcher.getBookDescription(volumes);
        if (result.isPresent()) {
            description = result.get();
        }

        return result.isPresent();
    }

    public boolean setCoverLinkFromBooksAPI(Volumes volumes) {
        Optional<String> result = BookSearcher.getBookCoverLink(volumes);
        if (result.isPresent()) {
            coverLink = result.get();
        }

        return result.isPresent();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================================\n");

        sb.append(title + "\n");
        sb.append(language + "\n");
        sb.append(coverLink + "\n");

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

    public List<LocationEntity> getCities() {
        return cities;
    }

    public void setCities(List<LocationEntity> cities) {
        this.cities = cities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BookAuthor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<BookAuthor> authors) {
        this.authors = authors;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
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
