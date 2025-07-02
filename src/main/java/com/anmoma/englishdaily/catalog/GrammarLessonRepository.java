package com.anmoma.englishdaily.catalog;

import java.util.List;
import java.util.Optional;

public interface GrammarLessonRepository {
    void create(GrammarLesson grammarLesson);

    List<GrammarLesson> findAll();

    List<GrammarLesson> findGrammarLessonsByLevel(GrammarLessonLevel level);

    Optional<GrammarLesson> findGrammarLessonById(Long grammarLessonId);
}
