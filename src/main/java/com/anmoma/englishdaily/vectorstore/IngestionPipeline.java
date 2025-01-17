package com.anmoma.englishdaily.vectorstore;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class IngestionPipeline {
    private final VectorStore vectorStore;
    private final TextSplitter textSplitter;
    private final FolderReader folderReader;

    IngestionPipeline(VectorStore vectorStore, TextSplitter textSplitter, FolderReader folderReader) {
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
        this.folderReader = folderReader;
    }

    public void populateVectorStore() {
        // Specify the folder path
        List<Path> filesList = folderReader.getFilenamesFromFolder("documents");
        filesList.forEach(f -> {
            Resource resource = new PathResource(f);
            var config = PdfDocumentReaderConfig.builder()
                                                /*.withPageExtractedTextFormatter(
                                                        new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(3)
                                                                                            .withNumberOfTopPagesToSkipBeforeDelete(1)
                                                                                            .build())*/.withPagesPerDocument(1)
                                                .build();
            var pdfReader = new PagePdfDocumentReader(resource, config);
            vectorStore.add(textSplitter.apply(pdfReader.get()));
        });
    }

}
