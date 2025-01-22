package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.IntegrationTest;
import com.anmoma.englishdaily.vectorstore.IngestionPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class VocabularyServiceTest {
    @Autowired
    private IngestionPipeline ingestionPipeline;
    @Autowired
    private VocabularyService vocabularyService;
    @Autowired
    private VectorStore vectorStore;

    @BeforeEach
    void setUp() {
        ingestionPipeline.populateVectorStore();
    }

    @Test
    void ingestionPipelineShouldIterateThroughDocumentsFolder() {
        VocabularyTerm dailyVocabulary = vocabularyService.getDailyVocabulary(List.of("yonks", "epic", "crush", "crash"));
        System.out.println(dailyVocabulary);
        assertThat(dailyVocabulary).isNotNull();
    }
}