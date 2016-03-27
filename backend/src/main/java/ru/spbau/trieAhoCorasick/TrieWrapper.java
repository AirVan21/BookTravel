package ru.spbau.trieAhoCorasick;

import org.ahocorasick.trie.Trie;

import java.util.List;
import java.util.Map;


/**
 * Created by airvan21 on 20.02.16.
 */
public class TrieWrapper {

    public static Trie buildTrie(Map<String, List<String>> searchWords) {
        Trie.TrieBuilder trieBuilder = Trie.builder().caseInsensitive().onlyWholeWordsWhiteSpaceSeparated();
        for (Map.Entry<String, List<String>> values : searchWords.entrySet()) {
            values.getValue().forEach(cityName -> trieBuilder.addKeyword(cityName));
            trieBuilder.addKeyword(values.getKey());
        }

        return trieBuilder.caseInsensitive().onlyWholeWordsWhiteSpaceSeparated().build();
    }
}
