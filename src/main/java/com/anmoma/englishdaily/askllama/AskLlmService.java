package com.anmoma.englishdaily.askllama;

import reactor.core.publisher.Flux;

public interface AskLlmService {
    Flux<String> ask(String userRequest);
}
