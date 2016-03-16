package ru.spbau.archiveManager;

import nl.siegmann.epublib.domain.Metadata;
import org.ahocorasick.trie.Trie;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import ru.spbau.database.BookRecord;
import ru.spbau.database.DataBaseRecord;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.locationRecord.LocationData;
import ru.spbau.locationRecord.LocationRecord;
import ru.spbau.nerWrapper.NERWrapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by airvan21 on 23.02.16.
 */
public class ArchiveManager {

    static public void generateDataBase(String pathToIndexFile, NERWrapper locationNER, Datastore ds, Trie trie) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile));

            for (String pathToBook; (pathToBook = reader.readLine()) != null;) {
                List<LocationData> locationList = processBook(pathToBook, locationNER, trie);
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);

                BookRecord bookRecord = new BookRecord(bookMetadata, locationList);
//                bookRecord.consoleLog();
                ds.save(bookRecord);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void handleBookArchive(String pathToIndexFile, NERWrapper locationNER, Trie trie) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile));

            for(String pathToBook; (pathToBook = reader.readLine()) != null; ) {
                List<LocationData> locationList = processBook(pathToBook, locationNER, trie);
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);
                DataBaseRecord dbRecord = new DataBaseRecord(bookMetadata, locationList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and analyses book content.
     * Validates location keyword via - a) via trained Trie match
     *                                  b)  via Google Geocoding API.
     * Returns location records.
     *
     * @param pathToBook
     * @param locationNER
     */
    static private List<LocationData> processBook(String pathToBook, NERWrapper locationNER, Trie trie) {
        List<LocationData> filteredLocationList = new ArrayList<>();
        try {
            String book = EPUBHandler.readFromPath(pathToBook);
            List<LocationRecord> rawLocationList = locationNER.classifyBook(book);

            for (LocationRecord location : rawLocationList) {
                System.out.print(location.getLocationData().keyword);
                if (location.validateKeyword(trie)) {
                    System.out.println(" -- OK");
                    filteredLocationList.add(location.getLocationData());
                } else {
                    System.out.println(" -- NOK");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filteredLocationList;
    }

}
