package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.catalog.GrammarLesson;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/englishdaily")
public class GrammarController {
    private final GrammarService grammarService;
    private final SocketIOServer socketIOServer;

    public GrammarController(GrammarService grammarService, SocketIOServer socketIOServer) {
        this.grammarService = grammarService;
        this.socketIOServer = socketIOServer;
    }

    @GetMapping("grammar")
    ResponseEntity<Void> askLlamaSimple(@RequestParam GrammarLesson grammarLesson) {
        Flux<String> llmResponse = grammarService.generateGrammarLesson(grammarLesson);
        llmResponse.subscribe(message -> {
            socketIOServer.getBroadcastOperations()
                          .sendEvent("message", message);
        });
        return ResponseEntity.ok()
                             .build();
    }
}
