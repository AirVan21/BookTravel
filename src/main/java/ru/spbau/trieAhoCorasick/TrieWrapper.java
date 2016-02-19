package ru.spbau.trieAhoCorasick;

import org.ahocorasick.trie.Trie;

import java.util.List;
import java.util.Map;


/**
 * Created by airvan21 on 20.02.16.
 */
public class TrieWrapper {

    public static Trie buildTrie(Map<String, List<String>> searchWords) {
        Trie.TrieBuilder trieBuilder = Trie.builder().onlyWholeWordsWhiteSpaceSeparated();
        for (List<String> values : searchWords.values()) {
            for (String value : values) {
                trieBuilder.addKeyword(value);
            }
        }

        return trieBuilder.build();
    }
}
