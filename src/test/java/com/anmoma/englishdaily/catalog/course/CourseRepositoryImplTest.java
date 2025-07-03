package com.anmoma.englishdaily.catalog.course;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class CourseRepositoryImplTest {
    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("El repositorio debe guardar datos")
    void givenCreateRepositoryOperationShouldCourseBePersisted() {
        courseRepository.create(Course.withTitleFolderPathAndVocabularySupported("Advanced Grammar Challenge", "advancedgrammarchallenge"));

        List<Course> grammarLessons = courseRepository.findAll();

        assertThat(grammarLessons).isNotEmpty()
                                  .anyMatch(c -> c.getTitle()
                                                  .equals("Advanced Grammar Challenge") && c.getFolderPath()
                                                                                            .equals("advancedgrammarchallenge"));
    }

    @Test
    @DisplayName("Find all debe devolver una lista no vacia de cursos")
    void findAllShouldReturnNotEmptyList() {
        List<Course> allCourses = courseRepository.findAll();

        assertThat(allCourses).isNotEmpty()
                              .hasSizeGreaterThan(2);
    }

    @Test
    @DisplayName("Un curso debe tener título, el path a sus lecciones y el flag que indica si soporta vocabulario o no")
    void courseShouldHaveTitleAndPathToLessonsAndSupportsVocabularyFlag() {
        List<Course> allCourses = courseRepository.findAll();

        Course courseUnderTest = allCourses.stream()
                                           .filter(c -> c.getTitle()
                                                         .equals("Advanced Grammar Challenge"))
                                           .findFirst()
                                           .orElseThrow();

        assertThat(courseUnderTest.getTitle()).isEqualTo("Advanced Grammar Challenge");
        assertThat(courseUnderTest.getFolderPath()).isEqualTo("advancedgrammarchallenge");
        assertThat(courseUnderTest.isVocabularySupported()).isFalse();
    }

    @Test
    @DisplayName("Hay cursos que tienen vocabulary bank")
    void givenFindAllVocabularySupportedCoursesShouldReturnTwo() {
        List<Course> vocabularySupportedCourses = courseRepository.findAllVocabularySupportedCourses();

        assertThat(vocabularySupportedCourses).hasSizeGreaterThanOrEqualTo(4);
    }
}