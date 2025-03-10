package com.anmoma.englishdaily.grammar;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/englishdaily")
public class GrammarController {
    private final GrammarLessonApplicationService grammarLessonApplicationService;
    private final SocketIOServer socketIOServer;

    public GrammarController(GrammarLessonApplicationService grammarLessonApplicationService, SocketIOServer socketIOServer) {
        this.grammarLessonApplicationService = grammarLessonApplicationService;
        this.socketIOServer = socketIOServer;
    }

    @GetMapping("grammar/{grammarLessonId}")
    ResponseEntity<Void> askLlamaSimple(@PathVariable Long grammarLessonId) {
        Flux<String> llmResponse = grammarLessonApplicationService.generateGrammarLesson(grammarLessonId);
        llmResponse.subscribe(message -> {
            socketIOServer.getBroadcastOperations()
                          .sendEvent("message", message);
        });
        return ResponseEntity.ok()
                             .build();
    }
}
