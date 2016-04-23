package ru.spbau.database;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.GeocodingResult;

import java.util.Optional;

public class CityCoordinates {
    private final static String provinceId = "ADMINISTRATIVE_AREA_LEVEL_1";
    private final static String countryId  = "COUNTRY";
    private String country  = "";
    private String province = "";
    private double lat;
    private double lng;

    public CityCoordinates() {}

    public CityCoordinates(String country, String province, double lat, double lng) {
        this.country = country;
        this.province = province;
        this.lat = lat;
        this.lng = lng;
    }

    public CityCoordinates(GeocodingResult location) {
        lat = location.geometry.location.lat;
        lng = location.geometry.location.lng;
        country  = getParameterFromGeocoding(location, countryId).get();
        province = getParameterFromGeocoding(location, provinceId).get();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    private Optional<String> getParameterFromGeocoding(GeocodingResult location, String parameter) {
        for (AddressComponent item : location.addressComponents) {
            if (item.types.length > 0 && item.types[0].name().equals(parameter)) {
                return Optional.of(item.longName);
            }
        }

        return Optional.of("");
    }
}
