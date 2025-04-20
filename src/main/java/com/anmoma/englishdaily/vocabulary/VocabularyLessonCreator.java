package com.anmoma.englishdaily.vocabulary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.IntStream.range;

@Service
public class VocabularyLessonCreator {
    private final VocabularyService vocabularyService;
    private final Integer maxVocabularyTerms;
    private final ApplicationEventPublisher applicationEventPublisher;

    public VocabularyLessonCreator(VocabularyService vocabularyService,
            @Value("${com.antoniomm.englishdaily.vocabulary-lessons.max-size}") Integer maxVocabularyTerms,
            ApplicationEventPublisher applicationEventPublisher) {
        this.vocabularyService = vocabularyService;
        this.maxVocabularyTerms = maxVocabularyTerms;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void generateVocabularyLesson() {
        List<VocabularyTerm> vocabularyTerms = range(0, maxVocabularyTerms).mapToObj(i -> vocabularyService.getDailyVocabulary())
                                                                           .toList();
        applicationEventPublisher.publishEvent(new VocabularyLessonCreated(vocabularyTerms));
    }
}
