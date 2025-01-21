package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.IntegrationTest;
import com.anmoma.englishdaily.LlmNotAvailableException;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    void givenGetToEndPointShouldInvokeServiceAndReturnAVocabularyResponse() {
        MockMvcTester mockMvcTester = MockMvcTester.create(mockMvc);
        given(vocabularyService.getDailyVocabulary()).willReturn(aRandomVocabularyTerm());

        assertThat(mockMvcTester.get()
                                .uri("/api/v1/englishdaily/vocabulary")).bodyJson()
                                                                        .extractingPath("$")
                                                                        .asMap()
                                                                        .contains(entry("source", "Slang Challenge (English with Lucy) 1.pdf"),
                                                                                entry("word", "pluck"),
                                                                                entry("definition", "to give yourself courage to do something"),
                                                                                entry("partOfSpeech", "verb"), entry("pronunciation", "/plʌk/"),
                                                                                entry("exampleSentence",
                                                                                        "We plucked up the courage to ask for a raise."),
                                                                                entry("collocations", Collections.emptyList()),
                                                                                entry("synonyms", List.of("the courage", "make oneself")));
        then(vocabularyService).should()
                               .getDailyVocabulary();
    }

    @Test
    @DisplayName("La lista negra debe pasarse al servicio ")
    void blackListShouldBeReadFromQueryParamAndPassItToService() {
        MockMvcTester mockMvcTester = MockMvcTester.create(mockMvc);
        given(vocabularyService.getDailyVocabulary(List.of("crash", "crush"))).willReturn(aRandomVocabularyTerm());

        assertThat(mockMvcTester.get()
                                .uri("/api/v1/englishdaily/vocabulary")
                                .queryParam("blacklist", "crash")
                                .queryParam("blacklist", "crush")).bodyJson()
                                                                  .extractingPath("$")
                                                                  .asMap()
                                                                  .contains(entry("source", "Slang Challenge (English with Lucy) 1.pdf"),
                                                                          entry("word", "pluck"),
                                                                          entry("definition", "to give yourself courage to do something"),
                                                                          entry("partOfSpeech", "verb"), entry("pronunciation", "/plʌk/"),
                                                                          entry("exampleSentence", "We plucked up the courage to ask for a raise."),
                                                                          entry("collocations", Collections.emptyList()),
                                                                          entry("synonyms", List.of("the courage", "make oneself")));
        then(vocabularyService).should()
                               .getDailyVocabulary(List.of("crash", "crush"));
    }

    @Test
    @DisplayName("Cuando Llama no está disponible debe devolver servicio no disponible")
    void whenLlamaIsNotAvailableShouldReturnNotAvailable() {
        MockMvcTester mockMvcTester = MockMvcTester.create(mockMvc);
        given(vocabularyService.getDailyVocabulary()).willThrow(new LlmNotAvailableException("Llama not available"));

        assertThat(mockMvcTester.get()
                                .uri("/api/v1/englishdaily/vocabulary")).hasStatus(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private VocabularyTerm aRandomVocabularyTerm() {
        return new VocabularyTerm("Slang Challenge (English with Lucy) 1.pdf", "pluck", "to give yourself courage to do something", "verb", "/plʌk/",
                "We plucked up the courage to ask for a raise.", Collections.emptyList(), List.of("the courage", "make oneself"));
    }

}