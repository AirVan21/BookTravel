package ru.spbau.archiveManager;

import nl.siegmann.epublib.domain.Metadata;
import ru.spbau.epubParser.EPUBHandler;
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

    static public void generateGsonArchive(String pathToIndexFile, String pathToOutputDir,
                                           NERWrapper locationNER) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile));

            for(String pathToBook; (pathToBook = reader.readLine()) != null; ) {
                List<LocationRecord> locationList = processBook(pathToBook, locationNER);
                Metadata bookMetadata = EPUBHandler.readBookMetadataFromPath(pathToBook);
                // Organize output to json via Gson
                System.out.println(bookMetadata.getAuthors());
                System.out.println(bookMetadata.getTitles());
                System.out.println(locationList.get(0).getLocationData().geocodingHelp[0].formattedAddress);
                System.out.println();
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
                List<LocationRecord> locationList = processBook(pathToBook, locationNER);
                // Some printing output information code
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
    static private List<LocationRecord> processBook(String pathToBook, NERWrapper locationNER) {
        List<LocationRecord> filteredLocationList = new ArrayList<LocationRecord>();
        try {
            String book = EPUBHandler.readFromPath(pathToBook);
            List<LocationRecord> rawLocationList = locationNER.classifyBook(book);

            for (LocationRecord location : rawLocationList) {
                if (location.validateKeyword()) {
                    filteredLocationList.add(location);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filteredLocationList;
    }

}
