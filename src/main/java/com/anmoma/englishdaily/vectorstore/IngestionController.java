package com.anmoma.englishdaily.vectorstore;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/englishdaily")
class IngestionController {
    private final IngestionPipeline ingestionPipeline;

    IngestionController(IngestionPipeline ingestionPipeline) {
        this.ingestionPipeline = ingestionPipeline;
    }

    @PostMapping("vector-store")
    ResponseEntity<Void> ingestAllData() {
        ingestionPipeline.populateVectorStore();
        return ResponseEntity.ok()
                             .build();
    }
}
