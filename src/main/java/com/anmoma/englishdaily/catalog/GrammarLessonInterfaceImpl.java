package com.anmoma.englishdaily.catalog;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GrammarLessonInterfaceImpl implements GrammarLessonRepository {
    private final GrammarLessonJpaRepository grammarLessonJpaRepository;

    public GrammarLessonInterfaceImpl(GrammarLessonJpaRepository grammarLessonJpaRepository) {
        this.grammarLessonJpaRepository = grammarLessonJpaRepository;
    }

    @Override
    public void create(GrammarLesson grammarLesson) {
        this.grammarLessonJpaRepository.save(grammarLesson);
    }

    @Override
    public List<GrammarLesson> findAll() {
        return grammarLessonJpaRepository.findAll();
    }

    @Override
    public List<GrammarLesson> findGrammarLessonsByLevel(GrammarLessonLevel level) {
        return grammarLessonJpaRepository.findAllByLevel(level);
    }

    @Override
    public Optional<GrammarLesson> findGrammarLessonById(Long grammarLessonId) {
        return grammarLessonJpaRepository.findById(grammarLessonId);
    }
}
