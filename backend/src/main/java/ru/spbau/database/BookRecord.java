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

    // TODO: toString
    public void consoleLog() {
        System.out.println("================================================================");

        System.out.println(title);
        System.out.println(language);

        System.out.println("-----------------------------------------------------------------");
        for (LocationEntity location : cities) {
            System.out.println(authors);
            System.out.println("--------------");
            System.out.println(location.getCityName());
            System.out.println("--------------");
            location.getQuotes().forEach(sentence -> System.out.println(sentence));
        }

        System.out.println("================================================================");
        System.out.println();
    }
}
