package ru.spbau;

import ru.spbau.archiveManager.ArchiveManager;
import ru.spbau.nerWrapper.NERWrapper;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String serializedClassifier = "./data/classifiers/english.all.3class.distsim.crf.ser.gz";
        String pathToSmallIndex = "./data//archive/book_index_small.txt";

        NERWrapper locationNER = new NERWrapper(serializedClassifier);
        ArchiveManager.handleBookArchive(pathToSmallIndex, locationNER);
    }
}
