package com.anmoma.englishdaily.vocabulary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/englishdaily")
class VocabularyController {
    private final VocabularyService vocabularyService;

    VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping("vocabulary")
    ResponseEntity<VocabularyResponse> getDailyVocabulary() {
        return ResponseEntity.ok(new VocabularyResponse(vocabularyService.getDailyVocabulary()));
    }

    record VocabularyResponse(String response) {
    }
}
