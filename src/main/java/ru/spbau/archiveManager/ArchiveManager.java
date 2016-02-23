package ru.spbau.archiveManager;

import edu.stanford.nlp.ling.CoreLabel;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.nerWrapper.NERWrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by airvan21 on 23.02.16.
 */
public class ArchiveManager {

    static public void handleBookArchive(String pathToIndexFile, NERWrapper locationNER) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(pathToIndexFile));

        for(String pathToBook; (pathToBook = reader.readLine()) != null; ) {
            parseBook(pathToBook, locationNER);
        }
    }

    /**
     * Simple template function for work test
     * @param pathToBook
     * @param locationNER
     */
    static private void parseBook(String pathToBook, NERWrapper locationNER) {
        try {
            String title = EPUBHandler.readBookTitleFromPath(pathToBook);
            System.out.println(title);

            String book = EPUBHandler.readFromPath(pathToBook);
            Map<String, List<CoreLabel>> locationMap = locationNER.classifyBook(book);

            for (String label : locationMap.keySet()) {
                System.out.print(label + " -> ");
                System.out.println(locationMap.get(label));
            }

            System.out.println();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
