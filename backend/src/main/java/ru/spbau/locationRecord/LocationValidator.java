package ru.spbau.locationRecord;

import org.mongodb.morphia.Datastore;
import ru.spbau.database.CityRecord;

import java.util.List;

/**
 * Created by airvan21 on 04.04.16.
 */
public class LocationValidator {
    public final Datastore validator;

    public LocationValidator(Datastore validator) {
        this.validator = validator;
    }

    /**
     * Validation of keyword via finding match in MongoDB
     *
     * @return true - valid location; false - invalid location
     */
    public boolean validateWithDB(String keyword) {
        List<CityRecord> query = validator.find(CityRecord.class).field("cityName").equal(keyword).asList();

        return !query.isEmpty();
    }
}
