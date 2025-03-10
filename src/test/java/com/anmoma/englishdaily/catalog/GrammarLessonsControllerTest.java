package com.anmoma.englishdaily.catalog;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

@IntegrationTest
class GrammarLessonsControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
}