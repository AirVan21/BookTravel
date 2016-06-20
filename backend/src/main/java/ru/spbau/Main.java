package ru.spbau;

import com.mongodb.MongoClient;
import com.opencsv.CSVReader;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import ru.spbau.archiveManager.BookDatabaseGenerator;
import ru.spbau.archiveManager.CityDatabaseGenerator;
import ru.spbau.csvHandler.CSVHandler;
import ru.spbau.csvHandler.CityEntry;
import ru.spbau.database.CityRecord;

import java.io.*;
import java.util.List;

public class Main {

    public static void runCitiesDBCreation() throws FileNotFoundException {
        final String pathToCSV = "./data/csv/cities/cities-part1.csv";

        final MongoClient mongo = new MongoClient();
        final Datastore datastore = new Morphia().createDatastore(mongo, "Cities");

        final CSVReader reader = new CSVReader(new FileReader(pathToCSV));
        final List<CityEntry> entries = CSVHandler.parse(reader);
        CityDatabaseGenerator.fillDatabase(entries, datastore);

        List<CityRecord> query = datastore.find(CityRecord.class).field("cityName").containsIgnoreCase("U.S.").asList();
    }

    public static void runBooksDBCreation() throws IOException, ClassNotFoundException {
        final String pathToSmallIndex = "./data/archive/book_index_small.txt";
        final String pathToSerializedClassifier = "./data/classifiers/english.all.3class.distsim.crf.ser.gz";

        final MongoClient mongoCity = new MongoClient();
        final Datastore citiesDatastore = new Morphia().createDatastore(mongoCity, "Cities");

        final MongoClient mongoBook = new MongoClient();
        final Datastore datastore = new Morphia().createDatastore(mongoBook, "Books");

        final AbstractSequenceClassifier<CoreLabel> serializedClassifier = CRFClassifier.getClassifier(pathToSerializedClassifier);
        BookDatabaseGenerator.generateBookDataBase(pathToSmallIndex, serializedClassifier, datastore, citiesDatastore);
    }

    public static void main(String[] args) throws Exception
    {
//        runCitiesDBCreation();
        runBooksDBCreation();
    }
}
