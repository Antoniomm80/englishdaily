package com.anmoma.englishdaily.askllama;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/englishdaily")
class AskLlamaController {
    private final AskLlmService askLlmService;
    private final SocketIOServer socketIOServer;

    AskLlamaController(AskLlmService askLlmService, SocketIOServer socketIOServer) {
        this.askLlmService = askLlmService;
        this.socketIOServer = socketIOServer;
    }

    @PostMapping("ask-llama")
    ResponseEntity<Void> askLlama(@RequestBody AskLlamaRequest question) {
        Flux<String> llmResponse = askLlmService.ask(question.question);
        llmResponse.subscribe(message -> {
            socketIOServer.getBroadcastOperations()
                          .sendEvent("message", message);
        });
        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("ask-llama")
    ResponseEntity<Void> askLlamaSimple(@RequestParam String question) {
        Flux<String> llmResponse = askLlmService.ask(question);
        llmResponse.subscribe(message -> {
            socketIOServer.getBroadcastOperations()
                          .sendEvent("message", message);
        });
        return ResponseEntity.ok()
                             .build();
    }

    record AskLlamaRequest(String question) {

    }
}
