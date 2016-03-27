package ru.spbau.database;

public class CityCoordinates {
    private String country;
    private String province;
    private double lat;
    private double lng;

    public CityCoordinates() {
    }

    public CityCoordinates(String country, String province, double lat, double lng) {
        this.country = country;
        this.province = province;
        this.lat = lat;
        this.lng = lng;
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
}
