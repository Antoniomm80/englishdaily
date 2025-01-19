package com.anmoma.englishdaily.vocabulary;

import java.util.List;

public record VocabularyTerm(String source, String word, String definition, String partOfSpeech, String pronunciation, String exampleSentence,
                             List<String> collocations, List<String> synonyms) {
}
