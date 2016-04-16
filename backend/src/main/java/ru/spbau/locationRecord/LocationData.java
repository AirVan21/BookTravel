package ru.spbau.locationRecord;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;

import java.util.List;

/**
 * Created by airvan21 on 28.02.16.
 */
public class LocationData {
    public String keyword;
    public String sentence;
    public List<GeocodingResultSimple> geocodingHelp;

    public LocationData() {}

    public LocationData(String keyFromText, List<CoreLabel> sentenceFromText) {
        keyword = keyFromText;
        // TODO: fix wrong space padding after prepositions
        sentence = Sentence.listToOriginalTextString(sentenceFromText);
        geocodingHelp = null;
    }
}
