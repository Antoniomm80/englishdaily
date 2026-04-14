package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.documentreader.SelectedDocument;

import java.util.ArrayList;
import java.util.List;

public interface VocabularyService {
    VocabularyTerm getDailyVocabulary(List<String> blacklist);

    VocabularyTerm getDailyVocabulary();

    default List<VocabularyTerm> getVocabularyTerms(SelectedDocument doc, int count, List<String> blacklist) {
        List<String> runningBlacklist = new ArrayList<>(blacklist);
        List<VocabularyTerm> terms = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                VocabularyTerm term = getDailyVocabulary(runningBlacklist);
                terms.add(term);
                runningBlacklist.add(term.word());
            } catch (Exception e) {
                break;
            }
        }
        return terms;
    }
}
