package com.anmoma.englishdaily.grammar;

import reactor.core.publisher.Flux;

public interface GrammarService {
    Flux<String> generateGrammarLesson(GrammarLesson lesson);
}
