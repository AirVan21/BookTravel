package ru.spbau.archiveManager;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordToSentenceProcessor;
import nl.siegmann.epublib.domain.Metadata;
import org.mongodb.morphia.Datastore;
import ru.spbau.books.processor.BookProcessor;
import ru.spbau.csvHandler.CityEntry;
import ru.spbau.database.BookRecord;
import ru.spbau.database.CityCoordinates;
import ru.spbau.database.CityRecord;
import ru.spbau.database.LocationPair;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.locationRecord.LocationValidator;
import ru.spbau.books.nerWrapper.NERWrapper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 23.02.16.
 */
public class ArchiveManager {

    static public void generateBookDataBase(String pathToIndexFile,  AbstractSequenceClassifier<CoreLabel> classifier,
                                            Datastore ds, Datastore validate) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile))) {
            LocationValidator validator = new LocationValidator(validate);

            for (String pathToBook; (pathToBook = reader.readLine()) != null;) {
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);

                if (!bookMetadata.getLanguage().equals("en")) {
                    continue;
                }

                List<LocationPair> locationList = processBook(pathToBook, classifier, validator);
                if (!locationList.isEmpty()) {
                    BookRecord bookRecord = new BookRecord(bookMetadata, locationList);
                    bookRecord.consoleLog();
//                    ds.save(bookRecord);
                }
            }
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
     *                                  b) via Google Geocoding API.
     * Returns location records.
     *
     */
    static private List<LocationPair> processBook(String pathToBook, AbstractSequenceClassifier<CoreLabel> classifier,
                                                  LocationValidator validator) throws IOException {

        BookProcessor processor = new BookProcessor(classifier);
        List<LocationPair> locationList = processor.processBook(pathToBook);


        locationList = locationList.stream()
                .filter(locationPair -> validator.validateWithDB(locationPair.cityName))
                .collect(Collectors.toList());

        return locationList;
    }

}
