package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.IntegrationTest;
import com.corundumstudio.socketio.SocketIOServer;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class GrammarControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockInBean(GrammarController.class)
    private GrammarLessonApplicationService grammarLessonApplicationService;
    @Autowired
    private SocketIOServer socketIOServer;

    @Test
    @DisplayName("Debería devolver una respuesta de OLlama")
    void givenPostRequestShouldInvokeOLlama() throws Exception {
        given(grammarLessonApplicationService.generateGrammarLesson(15L)).willReturn(
                Flux.just("Cleft sentences", "are used to emphasize a particular piece", "of new or important information."));

        mockMvc.perform(get("/api/v1/englishdaily/grammar/{grammarLessonId}", 15))
               .andExpect(status().isOk());

        then(socketIOServer.getBroadcastOperations()).should()
                                                     .sendEvent("message", "Cleft sentences");
        then(socketIOServer.getBroadcastOperations()).should()
                                                     .sendEvent("message", "are used to emphasize a particular piece");
        then(socketIOServer.getBroadcastOperations()).should()
                                                     .sendEvent("message", "of new or important information.");
    }
}