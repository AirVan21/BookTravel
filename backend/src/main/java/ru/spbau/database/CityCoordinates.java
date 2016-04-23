package ru.spbau.database;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.GeocodingResult;

public class CityCoordinates {
    private final static String provinceId = "ADMINISTRATIVE_AREA_LEVEL_1";
    private final static String countryId  = "POLITICAL";
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

        // [3].short name -- province // ADMINISTRATIVE_AREA_LEVEL_1
        // [4].short name -- country  // POLITICAL
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

    private void setProvinceFromGeocoding(GeocodingResult location) {
        for (AddressComponent item : location.addressComponents) {

        }
    }

    private void setCountryFromGeocoding() {

    }
}
