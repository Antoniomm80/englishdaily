package com.anmoma.englishdaily.vocabulary;

import java.util.List;

public interface VocabularyService {
    VocabularyTerm getDailyVocabulary(List<String> blacklist);

    VocabularyTerm getDailyVocabulary();
}
