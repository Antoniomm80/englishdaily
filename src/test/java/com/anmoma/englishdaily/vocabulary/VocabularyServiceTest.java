package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.IntegrationTest;
import com.anmoma.englishdaily.vectorstore.IngestionPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class VocabularyServiceTest {
    @Autowired
    private IngestionPipeline ingestionPipeline;
    @Autowired
    private VocabularyService vocabularyService;

    @BeforeEach
    void setUp() {
        ingestionPipeline.populateVectorStore();
    }

    @Test
    void ingestionPipelineShouldIterateThroughDocumentsFolder() {
        String dailyVocabulary = vocabularyService.getDailyVocabulary();
        System.out.println(dailyVocabulary);
        assertThat(dailyVocabulary).isNotBlank();
    }
}