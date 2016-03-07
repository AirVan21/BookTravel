package ru.spbau;

import com.mongodb.MongoClient;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import org.mongodb.morphia.query.Query;
import ru.spbau.archiveManager.ArchiveManager;
import ru.spbau.database.DataBaseRecord;
import ru.spbau.nerWrapper.NERWrapper;

import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception {
        String pathToSerializedClassifier = "./data/classifiers/english.all.3class.distsim.crf.ser.gz";
        String pathToSmallIndex = "./data/archive/book_index_small.txt";

        AbstractSequenceClassifier<CoreLabel> serializedClassifier = CRFClassifier.getClassifier(pathToSerializedClassifier);
        NERWrapper locationNER = new NERWrapper(serializedClassifier);

        MongoClient mongo = new MongoClient();
        Datastore datastore = new Morphia().createDatastore(mongo, "book_manager");
        ArchiveManager.generateDataBase(pathToSmallIndex, locationNER, datastore);

        Query<DataBaseRecord> query = datastore.createQuery(DataBaseRecord.class);
        List<DataBaseRecord> booksAndLocations = query.asList();
        System.out.println("Records in DB = " + booksAndLocations.size());
    }
}
