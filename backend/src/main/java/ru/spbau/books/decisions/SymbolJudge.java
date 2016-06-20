package ru.spbau.books.decisions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by airvan21 on 14.05.16.
 */
public class SymbolJudge implements Judge {
    private static List<String> source = Arrays.asList("[", "]", "{", "}", "CHAPTER", "APPENDIX", "%");
    private static Set<String> forbiddenCharacters = new HashSet<>(source);

    @Override
    public boolean shouldAccept(String sentence) {
        return !forbiddenCharacters
                .stream()
                .anyMatch(sentence::contains);
    }
}
