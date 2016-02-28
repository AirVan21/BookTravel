package ru.spbau;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import ru.spbau.archiveManager.ArchiveManager;
import ru.spbau.nerWrapper.NERWrapper;
import java.lang.reflect.Type;

public class Main {

    public static void main(String[] args) throws Exception {
        String pathToSerializedClassifier = "./data/classifiers/english.all.3class.distsim.crf.ser.gz";
        String pathToSmallIndex = "./data/archive/book_index_small.txt";

        AbstractSequenceClassifier<CoreLabel> serializedClassifier = CRFClassifier.getClassifier(pathToSerializedClassifier);
        NERWrapper locationNER = new NERWrapper(serializedClassifier);
        ArchiveManager.generateGsonArchive(pathToSmallIndex, "./data/archive/jsons", locationNER);

//        Gson gson = new Gson();
//        String json = gson.toJson(GeocodingApi.geocode(context, "Rome").await());
//
//        Type gsonType = new TypeToken<GeocodingResult[]>() {}.getType();
//        GeocodingResult[] test = gson.fromJson(json, gsonType);
//        System.out.println(test[0]);
    }
}
