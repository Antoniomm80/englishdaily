package com.anmoma.englishdaily.vocabulary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<String> blacklist = new ArrayList<>();
        List<VocabularyTerm> vocabularyTerms = new ArrayList<>();
        for (int i = 0; i < maxVocabularyTerms; i++) {
            VocabularyTerm dailyVocabulary = vocabularyService.getDailyVocabulary(blacklist);
            vocabularyTerms.add(dailyVocabulary);
            blacklist.add(dailyVocabulary.word());
        }
        applicationEventPublisher.publishEvent(new VocabularyLessonCreated(vocabularyTerms));
    }
}
