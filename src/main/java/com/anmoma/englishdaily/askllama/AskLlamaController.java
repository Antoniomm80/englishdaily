package com.anmoma.englishdaily.askllama;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/englishdaily")
class AskLlamaController {
    private final AskLlamaService askLlamaService;
    private final SocketIOServer socketIOServer;

    AskLlamaController(AskLlamaService askLlamaService, SocketIOServer socketIOServer) {
        this.askLlamaService = askLlamaService;
        this.socketIOServer = socketIOServer;
    }

    @PostMapping("ask-llama")
    ResponseEntity<Void> askLlama(@RequestBody AskLlamaRequest question) {
        Flux<String> llmResponse = askLlamaService.ask(question.question);
        llmResponse.subscribe(message -> {
            socketIOServer.getBroadcastOperations()
                          .sendEvent("message", message);
        });
        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("ask-llama")
    ResponseEntity<Void> askLlamaSimple(@RequestParam String question) {
        Flux<String> llmResponse = askLlamaService.ask(question);
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
