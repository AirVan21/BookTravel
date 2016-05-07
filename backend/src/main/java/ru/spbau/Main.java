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
import ru.spbau.database.BookRecord;
import ru.spbau.database.CityRecord;
import ru.spbau.books.statistics.BookStatistics;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void statisticsQuery() throws IOException {
        BookStatistics statistics = new BookStatistics();
        final String pathToTarget = "./data/csv/sentence_length.csv";
        final String pathToSentimental = "./data/csv/sentence_sentiment.csv";

        MongoClient mongo = new MongoClient();
        Datastore datastore = new Morphia().createDatastore(mongo, "Books");
        List<BookRecord> query = datastore.find(BookRecord.class).asList().subList(0, 10);

        for (BookRecord record : query) {
            if (record.cities != null) {
                statistics.addBookStatistics(record);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSentimental))) {
            statistics
                    .getSentimentScore()
                    .stream()
                    .forEach(score -> {
                        try {
                            writer.write(score.toString());
                            writer.write("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

    }

    public static void runCitiesDBCreation() throws FileNotFoundException {
        final String pathToCSV = "./data/csv/cities/cities-part1.csv";

        MongoClient mongo = new MongoClient();
        Datastore datastore = new Morphia().createDatastore(mongo, "Cities");

        CSVReader reader = new CSVReader(new FileReader(pathToCSV));
        List<CityEntry> entries = CSVHandler.parse(reader);
        CityDatabaseGenerator.fillDatabase(entries, datastore);

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
        BookDatabaseGenerator.generateBookDataBase(pathToSmallIndex, serializedClassifier, datastore, citiesDatastore);
    }

    public static void runBookSearchTest() throws Exception {
        final String title = "Jane Eyre";
    }

    public static void runCitiesRequest() {
        MongoClient mongo = new MongoClient();
        Datastore datastore = new Morphia().createDatastore(mongo, "Cities");

//        List<CityRecord> query = datastore.find(CityRecord.class).asList()
//                .stream()
//                .filter(item -> item.getLocations() == null)
//                .collect(Collectors.toList());

        List<CityRecord> london = datastore
                .find(CityRecord.class)
                .field("cityName")
                .containsIgnoreCase("Naples")
                .asList();

    }

    public static void main(String[] args) throws Exception
    {
//        runCitiesRequest();
//        runCitiesDBCreation();
        runBooksDBCreation();
//        runBookSearchTest();
//        statisticsQuery();
//        runGeoSearchTest();
    }
}
