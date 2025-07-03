package com.anmoma.englishdaily.catalog.grammarlesson;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateGrammarLesson {
    private final GrammarLessonRepository grammarLessonRepository;

    public CreateGrammarLesson(GrammarLessonRepository grammarLessonRepository) {
        this.grammarLessonRepository = grammarLessonRepository;
    }

    @Transactional
    public void create(CreateGrammarLessonCommand command) {
        grammarLessonRepository.create(GrammarLesson.grammarLessonWithTitleAndLevel(command.title(), command.level()));
    }
}
