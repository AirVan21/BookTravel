package ru.spbau.database;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Created by airvan21 on 23.03.16.
 */
@Entity("City")
public class CityRecord {
    @Id
    public ObjectId cityID;
    private String cityName;
    private List<CityCoordinates> locations;

    public CityRecord() {}

    public CityRecord(String cityName, List<CityCoordinates> locations) {
        this.cityName = cityName;
        this.locations = locations;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<CityCoordinates> getLocations() {
        return locations;
    }

    public void setLocations(List<CityCoordinates> locations) {
        this.locations = locations;
    }
}
