package com.anmoma.englishdaily.vocabulary;

import java.util.List;

public record VocabularyLessonCreated(List<VocabularyTerm> vocabularyTerms) {
    public String printLesson() {
        StringBuilder sb = new StringBuilder();
        for (VocabularyTerm term : vocabularyTerms) {
            sb.append(term.printTerm())
              .append(" ------------------------------- ")
              .append("\n");
        }
        return sb.toString();
    }
}
