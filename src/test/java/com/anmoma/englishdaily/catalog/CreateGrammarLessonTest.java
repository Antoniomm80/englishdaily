package com.anmoma.englishdaily.catalog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;

class CreateGrammarLessonTest {
    private final GrammarLessonRepository grammarLessonRepository = Mockito.mock(GrammarLessonRepository.class);
    private final CreateGrammarLesson createGrammarLesson = new CreateGrammarLesson(grammarLessonRepository);

    @Test
    @DisplayName("El caso de uso debe procesar el comando e invocar al repositorio")
    void givenCreateGrammarLessonUseCaseShouldProcessCommandAndInvokeRepository() {
        CreateGrammarLessonCommand command = new CreateGrammarLessonCommand("New Grammar Lesson", GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE);

        createGrammarLesson.create(command);

        then(grammarLessonRepository).should()
                                     .create(argThat(lesson -> lesson.getTitle()
                                                                     .equals("New Grammar Lesson") && lesson.getLevel()
                                                                                                            .equals(GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE)));

    }
}