package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.IntegrationTest;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.Collections;
import java.util.List;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@IntegrationTest
class VocabularyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockInBean(VocabularyController.class)
    private VocabularyService vocabularyService;

    @Test
    @DisplayName("Get a endpoint debe obtener ")
    void givenGetToEndPointShouldInvokeServiceAndReturnAVocabularyResponse() throws Exception {
        MockMvcTester mockMvcTester = MockMvcTester.create(mockMvc);
        given(vocabularyService.getDailyVocabulary()).willReturn(aRandomVocabularyTerm());

        assertThat(mockMvcTester.get()
                                .uri("/api/v1/englishdaily/vocabulary")).bodyJson()
                                                                        .extractingPath("$")
                                                                        .asMap()
                                                                        .contains(entry("word", "pluck"),
                                                                                entry("definition", "to give yourself courage to do something"),
                                                                                entry("partOfSpeech", "verb"), entry("pronunciation", "/plʌk/"),
                                                                                entry("exampleSentence",
                                                                                        "We plucked up the courage to ask for a raise."),
                                                                                entry("collocations", Collections.emptyList()),
                                                                                entry("synonyms", List.of("the courage", "make oneself")));
        then(vocabularyService).should()
                               .getDailyVocabulary();
    }

    private VocabularyTerm aRandomVocabularyTerm() {
        return new VocabularyTerm("pluck", "to give yourself courage to do something", "verb", "/plʌk/",
                "We plucked up the courage to ask for a raise.", Collections.emptyList(), List.of("the courage", "make oneself"));
    }

}