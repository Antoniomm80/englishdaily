package com.anmoma.englishdaily.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseJpaRepository extends JpaRepository<Course, Long> {
    List<Course> findByVocabularySupported(boolean vocabularySupported);

    List<Course> findAllByVocabularySupportedTrue();
}
