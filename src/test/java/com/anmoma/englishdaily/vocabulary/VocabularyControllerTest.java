package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.IntegrationTest;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

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
        given(vocabularyService.getDailyVocabulary()).willReturn("Random Vocabulary Term");

        assertThat(mockMvcTester.get()
                                .uri("/api/v1/englishdaily/vocabulary")).bodyJson()
                                                                        .extractingPath("$")
                                                                        .asMap()
                                                                        .contains(entry("response", "Random Vocabulary Term"));
        then(vocabularyService).should()
                               .getDailyVocabulary();
    }

}