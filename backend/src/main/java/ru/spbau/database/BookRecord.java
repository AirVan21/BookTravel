package ru.spbau.database;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Metadata;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.bson.types.ObjectId;
import ru.spbau.locationRecord.LocationData;

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
    public List<LocationPair> cities;

    public BookRecord() {}

    public BookRecord(Metadata metadata, List<LocationPair> locationsFromBook) {
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

    public void consoleLog() {
        System.out.println("================================================================");

        System.out.println(title);
        System.out.println(language);

        System.out.println("-----------------------------------------------------------------");
        for (LocationPair location : cities) {
            System.out.println("--------------");
            System.out.println(location.cityName);
            System.out.println("--------------");
            location.quotes.forEach(sentence -> System.out.println(sentence));
        }

        System.out.println("================================================================");
        System.out.println();
    }
}
