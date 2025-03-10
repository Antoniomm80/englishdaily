package com.anmoma.englishdaily.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrammarLessonJpaRepository extends JpaRepository<GrammarLesson, Long> {
    List<GrammarLesson> findAllByLevel(GrammarLessonLevel level);
}
