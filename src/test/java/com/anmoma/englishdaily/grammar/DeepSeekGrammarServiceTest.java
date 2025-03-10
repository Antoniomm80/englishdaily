package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.IntegrationTest;
import com.anmoma.englishdaily.catalog.GrammarLesson;
import com.anmoma.englishdaily.catalog.GrammarLessonLevel;
import com.anmoma.englishdaily.vectorstore.IngestionPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

@IntegrationTest
class DeepSeekGrammarServiceTest {
    @Autowired
    private IngestionPipeline ingestionPipeline;
    @Autowired
    private GrammarService grammarService;

    @BeforeEach
    void setUp() {
        ingestionPipeline.populateVectorStore();
    }

    @Test
    @DisplayName("Debería devolver una lección de gramática")
    @Disabled("Solo para testing manual")
    void shouldReturnAGrammarLesson() {
        GrammarLesson impersonalPassiveVoiceLesson = GrammarLesson.grammarLessonWithTitleAndLevel("Impersonal Passive Voice, Get/Have Structures",
                GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE);
        Flux<String> grammarLesson = grammarService.generateGrammarLesson(impersonalPassiveVoiceLesson);
        String string = grammarLesson.collectList()
                                     .block()
                                     .stream()
                                     .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                                     .toString();
        System.out.println(string);
    }
}