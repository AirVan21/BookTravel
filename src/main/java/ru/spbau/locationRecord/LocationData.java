package ru.spbau.locationRecord;

import edu.stanford.nlp.ling.CoreLabel;

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
        sentence = buildSentence(sentenceFromText);
        geocodingHelp = null;
    }

    private String buildSentence(List<CoreLabel> sentenceFromText) {
        StringBuilder buf = new StringBuilder();
        sentenceFromText.forEach(coreLabel -> buf.append(coreLabel.value()));

        return buf.toString();
    }
}
