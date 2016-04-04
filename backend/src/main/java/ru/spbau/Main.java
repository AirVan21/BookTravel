package ru.spbau;

import com.mongodb.MongoClient;
import com.opencsv.CSVReader;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import ru.spbau.archiveManager.ArchiveManager;
import ru.spbau.csvHandler.CSVHandler;
import ru.spbau.csvHandler.CityEntry;
import ru.spbau.database.BookRecord;
import ru.spbau.database.CityRecord;
import ru.spbau.nerWrapper.NERWrapper;
import ru.spbau.statistics.BookStatistics;

import java.io.*;
import java.util.List;

public class Main {

    public static void statisticsQuery() throws IOException {
        BookStatistics statistics = new BookStatistics();
        final String pathToTarget = "./data/csv/sentence_length.csv";

        MongoClient mongo = new MongoClient();
        Datastore datastore = new Morphia().createDatastore(mongo, "Books");
        List<BookRecord> query = datastore.find(BookRecord.class).asList();

        for (BookRecord record : query) {
            if (record.cities != null) {
                // TODO: fix records with empty cities (no not add them to DB)
                statistics.addBookStatistics(record);
            }
        }
    }

    public static void runCitiesDBCreation() throws FileNotFoundException {
        final String pathToCSV = "./data/csv/world_cities.csv";

        MongoClient mongo = new MongoClient();
        Datastore datastore = new Morphia().createDatastore(mongo, "Cities");

        CSVReader reader = new CSVReader(new FileReader(pathToCSV));
        List<CityEntry> entries = CSVHandler.parse(reader);
        ArchiveManager.generateCityDataBase(entries, datastore);

        List<CityRecord> query = datastore.find(CityRecord.class).field("cityName").containsIgnoreCase("U.S.").asList();
        System.out.println("SELECT COUNT = " + query.size());
    }

    public static void runBooksDBCreation() throws IOException, ClassNotFoundException {
        final String pathToSmallIndex = "./data/archive/book_index_small.txt";
        final String pathToSerializedClassifier = "./data/classifiers/english.all.3class.distsim.crf.ser.gz";

        MongoClient mongoCity = new MongoClient();
        Datastore citiesDatastore = new Morphia().createDatastore(mongoCity, "Cities");

        MongoClient mongoBook = new MongoClient();
        Datastore datastore = new Morphia().createDatastore(mongoBook, "Books");

        AbstractSequenceClassifier<CoreLabel> serializedClassifier = CRFClassifier.getClassifier(pathToSerializedClassifier);
        NERWrapper locationNER = new NERWrapper(serializedClassifier);

        ArchiveManager.generateBookDataBase(pathToSmallIndex, locationNER, datastore, citiesDatastore);
    }

    public static void main(String[] args) throws Exception {
        runBooksDBCreation();
    }
}
