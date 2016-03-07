package ru.spbau.archiveManager;

import nl.siegmann.epublib.domain.Metadata;
import org.mongodb.morphia.Datastore;
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

    static public void generateDataBase(String pathToIndexFile, NERWrapper locationNER, Datastore ds) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile));

            for (String pathToBook; (pathToBook = reader.readLine()) != null;) {
                List<LocationData> locationList = processBook(pathToBook, locationNER);
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);
                DataBaseRecord dbRecord = new DataBaseRecord(bookMetadata, locationList);
                ds.save(dbRecord);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void handleBookArchive(String pathToIndexFile, NERWrapper locationNER) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile));

            for(String pathToBook; (pathToBook = reader.readLine()) != null; ) {
                List<LocationData> locationList = processBook(pathToBook, locationNER);
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
     * Validates location - keyword via Google Geocoding API.
     * Returns location records.
     *
     * @param pathToBook
     * @param locationNER
     */
    static private List<LocationData> processBook(String pathToBook, NERWrapper locationNER) {
        List<LocationData> filteredLocationList = new ArrayList<>();
        try {
            String book = EPUBHandler.readFromPath(pathToBook);
            List<LocationRecord> rawLocationList = locationNER.classifyBook(book);

            for (LocationRecord location : rawLocationList) {
                if (location.validateKeyword()) {
                    filteredLocationList.add(location.getLocationData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filteredLocationList;
    }

}
