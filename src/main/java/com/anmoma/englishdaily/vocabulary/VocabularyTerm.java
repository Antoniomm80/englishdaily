package com.anmoma.englishdaily.vocabulary;

import java.util.List;

public record VocabularyTerm(String source, String word, String definition, String partOfSpeech, String pronunciation, String exampleSentence,
                             List<String> collocations, List<String> synonyms) {
    public VocabularyTerm(String source, VocabularyTerm vocabularyTerm) {
        this(source, vocabularyTerm.word(), vocabularyTerm.definition(), vocabularyTerm.partOfSpeech(), vocabularyTerm.pronunciation(),
                vocabularyTerm.exampleSentence(), vocabularyTerm.collocations(), vocabularyTerm.synonyms());
    }

    String printTerm() {
        return "Source: " + source + "\n" + "Word: " + word + "\n" + "Definition: " + definition + "\n" + "Part of Speech: " + partOfSpeech + "\n" +
                "Pronunciation: " + pronunciation + "\n" + "Example Sentence: " + exampleSentence + "\n" + "Collocations: " +
                String.join(", ", collocations) + "\n" + "Synonyms: " + String.join(", ", synonyms) + "\n";
    }
}
