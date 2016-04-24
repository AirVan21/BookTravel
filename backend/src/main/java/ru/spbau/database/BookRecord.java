package ru.spbau.database;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Metadata;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by airvan21 on 14.03.16.
 */
@Entity("Book")
public class BookRecord {
    @Id
    public ObjectId bookID;
    public String title;
    public String language;
    public List<BookAuthor> authors;
    public List<LocationEntity> cities;

    public BookRecord() {}

    public BookRecord(Metadata metadata, List<LocationEntity> locationsFromBook) {
        title = metadata.getFirstTitle();
        authors = convertAuthorsList(metadata.getAuthors());
        language = metadata.getLanguage();
        cities = locationsFromBook;
    }

    private List<BookAuthor> convertAuthorsList(List<Author> authors) {
        List<BookAuthor> authorList = new ArrayList<>();
        authors.forEach(author -> authorList.add(new BookAuthor(author)));

        return authorList;
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
}
