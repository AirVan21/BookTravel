package ru.spbau.locationRecord;

import com.google.maps.model.GeocodingResult;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by airvan21 on 28.02.16.
 */
public class LocationData {
    public String keyword;
    public List<CoreLabel> sentence;
    public GeocodingResult[] geocodingHelp;

    public LocationData(String keyFromText, List<CoreLabel> sentenceFromText) {
        keyword = keyFromText;
        sentence = sentenceFromText;
    }
}
