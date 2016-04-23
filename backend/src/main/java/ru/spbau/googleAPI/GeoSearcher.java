package ru.spbau.googleAPI;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import ru.spbau.database.CityCoordinates;
import ru.spbau.database.CityRecord;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by airvan21 on 23.04.16.
 */
public class GeoSearcher {
    private static final String GEO_API_CODE = "AIzaSyBPGuEnVZcQarLwzByVquiP4D-lmc2Q9OY";
    private static final GeoApiContext geoContext = new GeoApiContext().setApiKey(GEO_API_CODE);

    public static CityRecord requestGeoInfo(String location) throws Exception {
        List<CityCoordinates> geoInformation = Arrays.asList(GeocodingApi
                .geocode(geoContext, location)
                .await())
                .stream()
                .map(CityCoordinates::new)
                .collect(Collectors.toList());

        return new CityRecord("", geoInformation);
    }
}
