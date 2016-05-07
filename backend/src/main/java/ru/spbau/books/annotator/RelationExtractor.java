package ru.spbau.books.annotator;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by airvan21 on 04.05.16.
 */
public class RelationExtractor {
    public static Set<String> extractMainRelations(Annotation annotation) {
        Set<String> resultSet = new HashSet<>();

        for (CoreMap quote :  annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Collection<RelationTriple> triples = quote.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            triples
                    .stream()
                    .forEach(triple -> {
                        resultSet.add(triple.relationGloss());
                        resultSet.add(triple.subjectLemmaGloss());
                    });
        }

        return resultSet;
    }
}
