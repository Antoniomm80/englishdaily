package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.documentreader.FilenameProvider;
import com.anmoma.englishdaily.documentreader.SelectedDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VocabularyLessonCreator {
    private static final Logger log = LoggerFactory.getLogger(VocabularyLessonCreator.class);

    private final VocabularyService vocabularyService;
    private final FilenameProvider filenameProvider;
    private final Integer maxVocabularyTerms;
    private final ApplicationEventPublisher applicationEventPublisher;

    public VocabularyLessonCreator(VocabularyService vocabularyService,
            FilenameProvider filenameProvider,
            @Value("${com.antoniomm.englishdaily.vocabulary-lessons.max-size}") Integer maxVocabularyTerms,
            ApplicationEventPublisher applicationEventPublisher) {
        this.vocabularyService = vocabularyService;
        this.filenameProvider = filenameProvider;
        this.maxVocabularyTerms = maxVocabularyTerms;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void generateVocabularyLesson() {
        List<String> blacklist = new ArrayList<>();
        List<VocabularyTerm> vocabularyTerms = new ArrayList<>();

        // P4: fetch all available documents once — one batch of Drive API calls
        List<SelectedDocument> allDocuments;
        try {
            allDocuments = filenameProvider.getAllSelectedDocuments();
        } catch (Exception e) {
            log.error("Failed to retrieve vocabulary documents, aborting lesson generation", e);
            return;
        }

        // P5: select ceil(max/2) distinct files so multiple terms come from the same extraction
        Collections.shuffle(allDocuments);
        int targetFileCount = Math.min(allDocuments.size(), Math.max(1, (maxVocabularyTerms + 1) / 2));
        List<SelectedDocument> selectedDocs = allDocuments.subList(0, targetFileCount);

        int remaining = maxVocabularyTerms;
        for (int i = 0; i < selectedDocs.size() && remaining > 0; i++) {
            SelectedDocument doc = selectedDocs.get(i);
            int filesLeft = selectedDocs.size() - i;
            int termsFromThisFile = (remaining + filesLeft - 1) / filesLeft; // distribute evenly, ceiling

            try {
                List<VocabularyTerm> terms = vocabularyService.getVocabularyTerms(doc, termsFromThisFile, blacklist);
                vocabularyTerms.addAll(terms);
                terms.forEach(t -> blacklist.add(t.word()));
                remaining -= terms.size();
                log.debug("Got {}/{} requested terms from {}", terms.size(), termsFromThisFile, doc.filename());
            } catch (Exception e) {
                log.error("Failed to get vocabulary terms from {}, skipping", doc.filename(), e);
            }
        }

        if (vocabularyTerms.isEmpty()) {
            log.error("No vocabulary terms could be generated, lesson will not be published");
            return;
        }

        log.info("Publishing vocabulary lesson with {} terms", vocabularyTerms.size());
        applicationEventPublisher.publishEvent(new VocabularyLessonCreated(vocabularyTerms));
    }
}
