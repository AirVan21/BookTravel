package ru.spbau.epubParser;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
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
        String text = new String();

        for (SpineReference bookSection : spine.getSpineReferences()) {
            Resource res = bookSection.getResource();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
        }
        // Removes HTML tags
        return Jsoup.parse(text).text();
    }

    public static String readBookTitleFromPath(String pathToEPUB) throws IOException {
        EpubReader epubReader = new EpubReader();
        Book book = epubReader.readEpub(new FileInputStream(pathToEPUB));

        return book.getTitle();
    }


}
