package com.anmoma.englishdaily.vectorstore;

import com.anmoma.englishdaily.catalog.Course;
import com.anmoma.englishdaily.catalog.CourseRepository;
import com.anmoma.englishdaily.documentreader.FolderReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestionPipeline {
    private static final Logger log = LoggerFactory.getLogger(IngestionPipeline.class);
    private final VectorStore vectorStore;
    private final FolderReader folderReader;
    private final KeywordEnricher keywordEnricher;
    private final DocumentReader documentReader;
    private final CourseRepository courseRepository;
    private final VectorStoreItemRepository vectorStoreItemRepository;

    IngestionPipeline(VectorStore vectorStore, FolderReader folderReader, KeywordEnricher keywordEnricher, DocumentReader documentReader,
            CourseRepository courseRepository, VectorStoreItemRepository vectorStoreItemRepository) {
        this.vectorStore = vectorStore;
        this.folderReader = folderReader;
        this.keywordEnricher = keywordEnricher;
        this.documentReader = documentReader;
        this.courseRepository = courseRepository;
        this.vectorStoreItemRepository = vectorStoreItemRepository;
    }

    public void populateVectorStore() {
        List<Course> allCourses = courseRepository.findAll();
        allCourses.stream()
                  .map(Course::getFolderPath)
                  .forEach(folder -> {
                      List<String> filesList = folderReader.getFilenamesFromFolder(folder);
                      filesList.forEach(f -> {

                          log.debug("Reading file {}", f);
                          if (!vectorStoreItemRepository.findItemsByFileName(f)
                                                        .isEmpty()) {
                              log.debug("File {} already exists in vector store", f);
                              return;
                          }

                          vectorStore.add(readResource(f, folderReader.getResource(f)));
                      });
                  });
    }

    private List<Document> readResource(String path, Resource resource) {
        try {

            return documentReader.readResource(resource);
            //List<Document> enrichedDocuments = keywordEnricher.enrichDocuments(documentsToAdd);

        } catch (AssertionError e) {
            log.error("Error reading file {}", path, e);
        }
        return List.of();
    }

}
