package ru.spbau.locationRecord;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by airvan21 on 28.02.16.
 */
public class LocationRecord {

    private LocationData locationData;
    static final String googleAPICode = ""; // input your unique key

    public LocationRecord(String keyFromText, List<CoreLabel> sentenceFromText) {
        locationData = new LocationData(keyFromText, sentenceFromText);
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public boolean validateKeyword() {
        try {
            locationData.geocodingHelp = requestGeoInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (locationData.geocodingHelp.length > 0);
    }

    private GeocodingResult[] requestGeoInfo() throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey(googleAPICode);
        GeocodingResult[] geoInformation =  GeocodingApi.geocode(context, locationData.keyword).await();

        return geoInformation;
    }
}
