package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.catalog.GrammarLesson;
import reactor.core.publisher.Flux;

public interface GrammarService {
    Flux<String> generateGrammarLesson(GrammarLesson lesson);
}
