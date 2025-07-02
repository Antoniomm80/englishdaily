package com.anmoma.englishdaily.catalog;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class GrammarLessonRepositoryTest {
    @Autowired
    private GrammarLessonRepository repository;

    @Test
    @DisplayName("El repositorio debe guardar datos")
    void givenCreateRepositoryOperationShouldGrammarLessonBePersisted() {
        repository.create(GrammarLesson.grammarLessonWithTitleAndLevel("Grammar Lesson Title", GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE));

        List<GrammarLesson> grammarLessons = repository.findAll();

        assertThat(grammarLessons).isNotEmpty()
                                  .anyMatch(gl -> gl.getTitle()
                                                    .equals("Grammar Lesson Title") && gl.getLevel()
                                                                                         .equals(GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE));
    }

    @Test
    @DisplayName("Find All debe recuperar datos")
    void givenRepositoryFindAllOperationShouldReturnData() {
        List<GrammarLesson> grammarLessons = repository.findAll();
        assertThat(grammarLessons).isNotEmpty()
                                  .hasSizeGreaterThan(2);
    }

    @Test
    @DisplayName("Find by level debe obtener los cursos pertenecientes a ese nivel")
    void givenFindByLevelShouldReturnAllGrammarLessonsBelongingToTheLevel() {
        List<GrammarLesson> advancedGrammarChallengeLessons = repository.findGrammarLessonsByLevel(GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE);
        assertThat(advancedGrammarChallengeLessons).isNotEmpty()
                                                   .allMatch(gl -> gl.getLevel()
                                                                     .equals(GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE));
    }
}