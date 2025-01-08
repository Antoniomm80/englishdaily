package com.anmoma.englishdaily.vectorstore;

import com.anmoma.englishdaily.IntegrationTest;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class IngestionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IngestionController ingestionController;
    @MockInBean(IngestionController.class)
    private IngestionPipeline ingestionPipeline;

    @Test
    @DisplayName("Post a endpoint debe invocar a ingestion pipeline")
    void givenPostVerbToEndpoingShouldInvokeIngestionPipeline() throws Exception {
        mockMvc.perform(post("/api/v1/englishdaily/vector-store"))
               .andExpect(status().isOk());

        then(ingestionPipeline).should()
                               .populateVectorStore();
    }
}