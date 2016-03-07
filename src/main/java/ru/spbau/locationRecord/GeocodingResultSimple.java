package ru.spbau.locationRecord;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;

/**
 * Created by airvan21 on 07.03.16.
 */
public class GeocodingResultSimple {
    public AddressComponent[] addressComponents;
    public String formattedAddress;
    public String[] postcodeLocalities;
    public AddressType[] types;
    public String placeId;

    public GeocodingResultSimple() {}

    public GeocodingResultSimple(GeocodingResult result) {
        addressComponents = result.addressComponents;
        formattedAddress = result.formattedAddress;
        postcodeLocalities = result.postcodeLocalities;
        types = result.types;
        placeId = result.placeId;
    }
}
