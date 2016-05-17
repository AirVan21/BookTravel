package ru.spbau.archiveManager;

import org.mongodb.morphia.Datastore;
import ru.spbau.csvHandler.CityEntry;
import ru.spbau.database.CityRecord;
import ru.spbau.googleAPI.GeoSearcher;

import java.util.List;

/**
 * CityDatabaseGenerator - class for generating database records from CityEntries
 */
public class CityDatabaseGenerator {
    static final double POPULATION_THRESHOLD = 10_000;

    /**
     * Adds information about cities to MongoDB.
     *
     * - Cities will be filtered according to POPULATION_THRESHOLD agreement
     * - Cities with duplicating names will be stored in one record
     * - Information about coordinates will be asked from Google Geocoding API via GeoSearcher class
     *
     * @param cityEntries - information about cities from csv file
     * @param ds          - interface for MongoDB for storing records
     */
    static public void fillDatabase(List<CityEntry> cityEntries, Datastore ds) {
        cityEntries
                .stream()
                .filter(city -> city.getPopulation() > POPULATION_THRESHOLD)
                .distinct()
                .map(CityEntry::getFormattedName)
                .map(cityName -> new CityRecord(cityName, GeoSearcher.getCityCoordinates(cityName)))
                .filter(record -> record.getLocations() != null)
                .forEach(ds::save);
    }
}
