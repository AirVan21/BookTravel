package ru.spbau.archiveManager;

import org.mongodb.morphia.Datastore;
import ru.spbau.csvHandler.CityEntry;
import ru.spbau.database.CityRecord;
import ru.spbau.googleAPI.GeoSearcher;

import java.util.List;

/**
 * Should be tested!
 */
public class CityDatabaseGenerator {
    static final double POPULATION_THRESHOLD = 10_000;

    static public void fillDatabase(List<CityEntry> cityEntries, Datastore ds) {
        cityEntries
                .stream()
                .filter(city -> city.getPopulation() > POPULATION_THRESHOLD)
                .distinct()
                .map(CityEntry::getFormattedName)
                .map(cityName -> new CityRecord(cityName, GeoSearcher.getCityCoordinates(cityName)))
                .forEach(record -> ds.save(record));
    }
}
