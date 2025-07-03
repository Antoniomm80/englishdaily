package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.catalog.grammarlesson.GrammarLesson;
import com.anmoma.englishdaily.catalog.grammarlesson.GrammarLessonRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GrammarLessonApplicationService {
    private final GrammarLessonRepository grammarLessonRepository;
    private final GrammarService grammarService;

    public GrammarLessonApplicationService(GrammarLessonRepository grammarLessonRepository, GrammarService grammarService) {
        this.grammarLessonRepository = grammarLessonRepository;
        this.grammarService = grammarService;
    }

    public Flux<String> generateGrammarLesson(Long grammarLessonId) {
        GrammarLesson lesson = grammarLessonRepository.findGrammarLessonById(grammarLessonId)
                                                      .orElseThrow(IllegalArgumentException::new);
        return grammarService.generateGrammarLesson(lesson);
    }
}
