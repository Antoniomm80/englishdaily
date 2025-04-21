package com.anmoma.englishdaily.vocabulary;

import java.util.List;

public class VocabularyTermMother {
    private String source;
    private String word;
    private String definition;
    private String partOfSpeech;
    private String pronunciation;
    private String exampleSentence;
    private List<String> collocations;
    private List<String> synonyms;

    public static VocabularyTermMother aVocabularyTerm() {
        return new VocabularyTermMother().withSource("EWL B2 - 5.7 Vocabulary Lesson Script.pdf")
                                         .withWord("to beaver away")
                                         .withDefinition("to work very hard at something for a long time")
                                         .withPartOfSpeech("phrasal verb")
                                         .withPronunciation("/ˈbiː.vər əˈweɪ/")
                                         .withExampleSentence("He was beavering away at his computer all night.")
                                         .withCollocations(List.of("beaver away at something", "beaver away on something"))
                                         .withSynonyms(List.of("to work hard", "to toil"));
    }

    public VocabularyTermMother withSource(String source) {
        this.source = source;
        return this;
    }

    public VocabularyTermMother withWord(String word) {
        this.word = word;
        return this;
    }

    public VocabularyTermMother withDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    public VocabularyTermMother withPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
        return this;
    }

    public VocabularyTermMother withPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
        return this;
    }

    public VocabularyTermMother withExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
        return this;
    }

    public VocabularyTermMother withCollocations(List<String> collocations) {
        this.collocations = collocations;
        return this;
    }

    public VocabularyTermMother withSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
        return this;
    }

    public VocabularyTerm build() {
        return new VocabularyTerm(source, word, definition, partOfSpeech, pronunciation, exampleSentence, collocations, synonyms);
    }
}