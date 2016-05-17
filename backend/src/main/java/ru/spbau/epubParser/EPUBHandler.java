package ru.spbau.epubParser;

import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;

import java.io.*;

/**
 * Created by airvan21 on 19.02.16.
 */
public class EPUBHandler {

    public static String readFromPath(String pathToEPUB) throws IOException {
        EpubReader epubReader = new EpubReader();
        Book book = epubReader.readEpub(new FileInputStream(pathToEPUB));
        Spine spine = new Spine(book.getTableOfContents());
        final StringBuilder text = new StringBuilder();

        for (SpineReference bookSection : spine.getSpineReferences()) {
            final BufferedReader reader = new BufferedReader(bookSection.getResource().getReader());

            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append(" ");
            }
        }
        // Removes HTML tags
        return Jsoup.parse(text.toString()).text();
    }

    public static Metadata readBookMetadataFromPath(String pathToEPUB) throws IOException {
        EpubReader epubReader = new EpubReader();
        Book book = epubReader.readEpub(new FileInputStream(pathToEPUB));

        return book.getMetadata();
    }

    /**
     * Checks EPUBs metadata structure
     * @param metadata - books meta-info from epublib
     * @return true    - approves book
     *         false   - rejects book
     */
    public static boolean isEPUBValid(Metadata metadata) {
        if (!metadata.getLanguage().equals("en")) {
            return false;
        }

        if (metadata.getFirstTitle().equals("")) {
            return  false;
        }

        if (metadata.getAuthors().size() == 0) {
            return false;
        }

        if (metadata.getAuthors().get(0).getFirstname().equals("") ||
                metadata.getAuthors().get(0).getLastname().equals("")) {
            return false;
        }

        return true;
    }
}
