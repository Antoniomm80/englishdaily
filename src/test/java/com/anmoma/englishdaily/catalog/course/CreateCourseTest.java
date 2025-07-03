package com.anmoma.englishdaily.catalog.course;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;

class CreateCourseTest {
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final CreateCourse createCourse = new CreateCourse(courseRepository);

    @Test
    @DisplayName("El caso de uso debe procesar el comando e invocar al repositorio")
    void givenCreateCourseUseCaseShouldProcessCommandAndInvokeRepository() {
        CreateCourseCommand command = new CreateCourseCommand("New Grammar Lesson", "advancedgrammarchallenge", true);

        createCourse.create(command);

        then(courseRepository).should()
                              .create(argThat(course -> course.getTitle()
                                                              .equals("New Grammar Lesson") && course.getFolderPath()
                                                                                                     .equals("advancedgrammarchallenge") &&
                                      course.isVocabularySupported()));

    }
}