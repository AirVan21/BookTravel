package ru.spbau.locationRecord;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by airvan21 on 28.02.16.
 */
public class LocationRecord {

    private LocationData locationData;
    /**
     * googleAPICode - is unique key per account
     */
    private static final String googleAPICode = "";
    private static final GeoApiContext context = new GeoApiContext().setApiKey(googleAPICode);

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
            System.out.println("Google geocoding API threw Exception on input = "
                    + locationData.keyword);
            System.out.println("Awaiting was unsuccessful!");
        }

        return (locationData.geocodingHelp.size() > 0);
    }

    private List<GeocodingResultSimple> requestGeoInfo() throws Exception {
        GeocodingResult[] geoInformation =  GeocodingApi.geocode(context, locationData.keyword).await();
        List<GeocodingResultSimple> geoResult = new ArrayList<>();
        for (GeocodingResult info : geoInformation) {
            geoResult.add(new GeocodingResultSimple(info));
        }

        return geoResult;
    }
}
