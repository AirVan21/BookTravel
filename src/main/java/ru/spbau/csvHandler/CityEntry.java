package ru.spbau.csvHandler;

import com.opencsv.bean.CsvBind;

/**
 * Created by airvan21 on 24.03.16.
 */
public class CityEntry extends CSVEntry {
    @CsvBind
    private String formattedName;
    @CsvBind
    private double lat;
    @CsvBind
    private double lng;
    @CsvBind
    private double population;
    @CsvBind
    private String country;
    @CsvBind
    private String province;

    public CityEntry() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFormattedName() {
        return formattedName;
    }

    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getPopulation() {
        return population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
