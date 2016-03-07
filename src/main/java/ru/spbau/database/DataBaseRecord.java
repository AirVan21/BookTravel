package ru.spbau.database;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Metadata;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import ru.spbau.locationRecord.LocationData;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by airvan21 on 06.03.16.
 */
@Entity("Book")
public class DataBaseRecord {
    @Id
    public ObjectId id;
    public String title;
    public String language;
    public List<BookAuthor> authors;
    public List<LocationData> locationDataFromBook;

    public DataBaseRecord() {}

    public DataBaseRecord(Metadata metadata, List<LocationData> locationsFromBook) {
        title = metadata.getFirstTitle();
        authors = convertAuthorsList(metadata.getAuthors());
        language = metadata.getLanguage();
        locationDataFromBook = locationsFromBook;
    }

    private List<BookAuthor> convertAuthorsList(List<Author> authors) {
        List<BookAuthor> authorList = new ArrayList<>();
        authors.forEach(author -> authorList.add(new BookAuthor(author)));

        return authorList;
    }
}
