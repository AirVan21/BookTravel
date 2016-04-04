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

    public BookRecord(Metadata metadata, List<LocationData> locationsFromBook) {
        title = metadata.getFirstTitle();
        authors = convertAuthorsList(metadata.getAuthors());
        language = metadata.getLanguage();
        cities = convertLocationDataToSet(locationsFromBook);
    }

    private List<BookAuthor> convertAuthorsList(List<Author> authors) {
        List<BookAuthor> authorList = new ArrayList<>();
        authors.forEach(author -> authorList.add(new BookAuthor(author)));

        return authorList;
    }

    private List<LocationPair> convertLocationDataToSet(List<LocationData> locationsFromBook) {
        Map<String, List<String>> citiesMap = new TreeMap<>();
        for (LocationData location : locationsFromBook) {
            if (citiesMap.containsKey(location.keyword)) {
                citiesMap.get(location.keyword).add(location.sentence);
            } else {
                List<String> sentenceList = new ArrayList<>();
                sentenceList.add(location.sentence);
                citiesMap.put(location.keyword, sentenceList);
            }
        }

        List<LocationPair> citiesList = new ArrayList<>();
        citiesMap.entrySet().forEach(location -> citiesList.add(new LocationPair(location.getKey(), location.getValue())));

        return citiesList;
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
