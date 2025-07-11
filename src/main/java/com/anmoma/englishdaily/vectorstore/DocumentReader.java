package com.anmoma.englishdaily.vectorstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig.ALL_PAGES;

@Component
class DocumentReader {
    private static final Logger log = LoggerFactory.getLogger(DocumentReader.class);
    private final TextSplitter textSplitter;
    private final boolean enableTextSplitter;

    DocumentReader(TextSplitter textSplitter, @Value("${com.antoniomm.englishdaily.enable-token-splitter}") boolean enableTextSplitter) {
        this.textSplitter = textSplitter;
        this.enableTextSplitter = enableTextSplitter;
    }

    List<Document> readResource(Resource resource) {
        var config = PdfDocumentReaderConfig.builder()
                                            .withPagesPerDocument(ALL_PAGES)
                                            .build();
        var pdfReader = new PagePdfDocumentReader(resource, config);
        List<Document> pdfDocuments = pdfReader.get();
        if (!enableTextSplitter && !pdfDocuments.isEmpty()) {
            log.debug("Returning documents from PDF reader without text splitting for {}", resource.getFilename());
            return pdfDocuments;
        }
        List<Document> documentsToAdd = textSplitter.apply(pdfDocuments);
        if (documentsToAdd.isEmpty()) {
            log.debug("Falling back to Tika reader for {}", resource.getFilename());
            var tikaReader = new TikaDocumentReader(resource);
            documentsToAdd = textSplitter.apply(tikaReader.get());
            log.debug("documents from tika reader {}", documentsToAdd.size());
        }
        return documentsToAdd;
    }
}
