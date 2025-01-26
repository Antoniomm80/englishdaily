package com.anmoma.englishdaily.askllama;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class AskLlamaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debería devolver una respuesta de Llama")
    void givenPostRequestShouldInvokeLlama() throws Exception {
        mockMvc.perform(post("/api/v1/englishdaily/ask-llama").contentType(MediaType.APPLICATION_JSON)
                                                              .content("{\"question\": \"¿Cómo se llama el protagonista de la historia?\"}"))
               .andDo(print())
               .andExpect(status().isOk());
    }
}