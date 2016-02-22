package ru.spbau;

import edu.stanford.nlp.ling.CoreLabel;
import org.ahocorasick.trie.Emit;
import ru.spbau.epubParser.EPUBHandler;
import ru.spbau.jsonParser.JSONHandler;
import ru.spbau.nerWrapper.NERWrapper;
import ru.spbau.trieAhoCorasick.TrieWrapper;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String serializedClassifier = "./data/classifiers/english.all.3class.distsim.crf.ser.gz";
        String jsonPath = "./data/countriesToCitiesFile.json";
        String bookPath = "./data/IAmLegend.epub";

        Map<String, List<String>> cityData = JSONHandler.readFromPath(jsonPath);
        String book = EPUBHandler.readFromPath(bookPath);
        Collection<Emit> entires = TrieWrapper.buildTrie(cityData).parseText(book);


        NERWrapper locationNER = new NERWrapper(serializedClassifier);
        Map<String, List<CoreLabel>> locationMap = locationNER.classifyBook(book);

        for (String label : locationMap.keySet()) {
            System.out.print(label + " -> ");
            System.out.println(locationMap.get(label));
        }

    }
}
