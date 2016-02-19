package ru.spbau;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.ahocorasick.trie.Emit;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.jsonParser.JSONHandler;
import ru.spbau.trieAhoCorasick.TrieWrapper;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String jsonPath = "./data/countriesToCitiesFile.json";
        String bookPath = "./data/IAmLegend.epub";

        Map<String, List<String>> cityData = JSONHandler.readFromPath(jsonPath);
        String book = EPUBHandler.readFromPath(bookPath);
        Collection<Emit> entires = TrieWrapper.buildTrie(cityData).parseText(book);
    }
}
