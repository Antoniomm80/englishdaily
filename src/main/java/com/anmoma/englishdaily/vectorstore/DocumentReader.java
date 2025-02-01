package com.anmoma.englishdaily.vectorstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DocumentReader {
    private static final Logger log = LoggerFactory.getLogger(DocumentReader.class);
    private final TextSplitter textSplitter;

    DocumentReader(TextSplitter textSplitter) {
        this.textSplitter = textSplitter;
    }

    List<Document> readResource(Resource resource) {
        var config = PdfDocumentReaderConfig.builder()
                                            .withPagesPerDocument(1)
                                            .build();
        var pdfReader = new PagePdfDocumentReader(resource, config);
        List<Document> documentsToAdd = textSplitter.apply(pdfReader.get());
        if (documentsToAdd.isEmpty()) {
            log.debug("Falling back to Tika reader for {}", resource.getFilename());
            var tikaReader = new TikaDocumentReader(resource);
            documentsToAdd = textSplitter.apply(tikaReader.get());
            log.debug("documents from tika reader {}", documentsToAdd.size());
        }
        return documentsToAdd;
    }
}
