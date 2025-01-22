package com.anmoma.englishdaily.vectorstore;

import com.anmoma.englishdaily.documentreader.FolderReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class IngestionPipeline {
    private static final Logger log = LoggerFactory.getLogger(IngestionPipeline.class);
    private final VectorStore vectorStore;
    private final TextSplitter textSplitter;
    private final FolderReader folderReader;
    private final KeywordEnricher keywordEnricher;

    IngestionPipeline(VectorStore vectorStore, TextSplitter textSplitter, FolderReader folderReader, KeywordEnricher keywordEnricher) {
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
        this.folderReader = folderReader;
        this.keywordEnricher = keywordEnricher;
    }

    public void populateVectorStore() {
        // Specify the folder path
        List<Path> filesList = folderReader.getFilenamesFromFolder("documents");
        filesList.forEach(f -> {
            log.info("Reading file {}", f.getFileName()
                                         .toString());
            Resource resource = new PathResource(f);
            var config = PdfDocumentReaderConfig.builder()
                                                /*.withPageExtractedTextFormatter(
                                                        new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(3)
                                                                                            .withNumberOfTopPagesToSkipBeforeDelete(1)
                                                                                            .build())*/.withPagesPerDocument(1)
                                                .build();
            var pdfReader = new PagePdfDocumentReader(resource, config);
            List<Document> documentsToAdd = textSplitter.apply(pdfReader.get());
            //fallback to tika reader
            if (documentsToAdd.isEmpty()) {
                log.info("Falling back to Tika reader for {}", f.getFileName()
                                                                .toString());
                var tikaReader = new TikaDocumentReader(resource);
                documentsToAdd = textSplitter.apply(tikaReader.get());
                log.info("documents from tika reader {}", documentsToAdd.size());
            }
            vectorStore.add(documentsToAdd);
        });
    }

}
