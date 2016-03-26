package ru.spbau.archiveManager;

import nl.siegmann.epublib.domain.Metadata;
import org.mongodb.morphia.Datastore;
import ru.spbau.csvHandler.CityEntry;
import ru.spbau.database.BookRecord;
import ru.spbau.database.CityCoordinates;
import ru.spbau.database.CityRecord;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.locationRecord.LocationData;
import ru.spbau.locationRecord.LocationRecord;
import ru.spbau.nerWrapper.NERWrapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by airvan21 on 23.02.16.
 */
public class ArchiveManager {

    static public void generateBookDataBase(String pathToIndexFile, NERWrapper locationNER,
                                            Datastore ds, Datastore validate) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile))) {
            for (String pathToBook; (pathToBook = reader.readLine()) != null;) {
                List<LocationData> locationList = processBook(pathToBook, locationNER, validate);
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);

                BookRecord bookRecord = new BookRecord(bookMetadata, locationList);
                if (bookRecord.language.equals("en")) {
                    bookRecord.consoleLog();
                    ds.save(bookRecord);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void generateCityDataBase(List<CityEntry> entires, Datastore ds) {
        Map<String, List<CityCoordinates>> storage = new HashMap<>();
        final double populationThreshold = 10_000;

        for (CityEntry city : entires) {
            if (city.getPopulation() > populationThreshold) {
                storage.putIfAbsent(city.getFormattedName(), new ArrayList<>());
                storage.get(city.getFormattedName()).add(new CityCoordinates(
                        city.getCountry(), city.getProvince(), city.getLat(), city.getLng()
                ));
            }
        }

        for (Map.Entry<String, List<CityCoordinates>> item : storage.entrySet()) {
            CityRecord record = new CityRecord(item.getKey(), item.getValue());
            ds.save(record);
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
    static private List<LocationData> processBook(String pathToBook, NERWrapper locationNER, Datastore validate) {
        List<LocationData> filteredLocationList = new ArrayList<>();
        try {
            String book = EPUBHandler.readFromPath(pathToBook);
            List<LocationRecord> rawLocationList = locationNER.classifyBook(book);

            for (LocationRecord location : rawLocationList) {
                if (location.validateKeywordDB(validate)) {
                    filteredLocationList.add(location.getLocationData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filteredLocationList;
    }

}
