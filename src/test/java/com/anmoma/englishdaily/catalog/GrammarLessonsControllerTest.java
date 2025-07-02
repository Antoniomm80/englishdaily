package com.anmoma.englishdaily.catalog;

import com.anmoma.englishdaily.IntegrationTest;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class GrammarLessonsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockInBean(GrammarLessonsController.class)
    private CreateGrammarLesson createGrammarLesson;

    @Test
    @DisplayName("Get al endpoint de lista de lecciones de gramática por nivel, debería devolver una lista de lecciones")
    void givenGetToEndPointShouldGetGrammarLessonsCatalog() {
        MockMvcTester mockMvcTester = MockMvcTester.create(mockMvc);

        assertThat(mockMvcTester.get()
                                .uri("/api/v1/englishdaily/grammar-lessons/{levelId}", 3)).bodyJson()
                                                                                          .extractingPath("$.lessons")
                                                                                          .asInstanceOf(list(LinkedHashMap.class))
                                                                                          .isNotEmpty()
                                                                                          .anySatisfy(lesson -> {
                                                                                              assertThat(lesson.get("title")).isEqualTo(
                                                                                                      "Relative and Participle Clauses");
                                                                                          });
    }

    @Test
    @DisplayName("Post al endpoint de creación de lecciones de gramática, debería crear una lección")
    void givenPostToEndPointShouldCreateGrammarLesson() throws Exception {
        mockMvc.perform(post("/api/v1/englishdaily/grammar-lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Grammar Lesson\", \"level\": \"ADVANCED_GRAMMAR_CHALLENGE\"}"))
                .andExpect(status().isOk());

        then(createGrammarLesson).should()
                .create(new CreateGrammarLessonCommand("New Grammar Lesson", GrammarLessonLevel.ADVANCED_GRAMMAR_CHALLENGE));
    }
}
