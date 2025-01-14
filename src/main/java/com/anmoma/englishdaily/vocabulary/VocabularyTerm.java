package com.anmoma.englishdaily.vocabulary;

import java.util.List;

public record VocabularyTerm(/*"word": "pluck",
                                     "definition": "to give yourself courage to do something",
                                     "part_of_speech": "verb",
                                     "pronunciation": "/plʌk/",
                                     "example_sentence": "We plucked up the courage to ask for a raise.",
                                     "collocations": ["the courage", "make oneself"],
                                     "synonyms":*/
        String word, String definition, String partOfSpeech, String pronunciation, String exampleSentence, List<String> collocations,
        List<String> synonyms) {
}
