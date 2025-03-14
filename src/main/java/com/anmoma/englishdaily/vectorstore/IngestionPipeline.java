package com.anmoma.englishdaily.vectorstore;

import com.anmoma.englishdaily.catalog.Course;
import com.anmoma.englishdaily.catalog.CourseRepository;
import com.anmoma.englishdaily.documentreader.FolderReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
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
                      List<Path> filesList = folderReader.getFilenamesFromFolder("documents/" + folder);
                      filesList.forEach(f -> {
                          String filename = f.getFileName()
                                             .toString();
                          log.debug("Reading file {}", filename);
                          if (!vectorStoreItemRepository.findItemsByFileName(filename)
                                                        .isEmpty()) {
                              log.debug("File {} already exists in vector store", filename);
                              return;
                          }
                          Resource resource = new PathResource(f);
                          vectorStore.add(readResource(f, resource));
                      });
                  });
    }

    private List<Document> readResource(Path f, Resource resource) {
        try {

            return documentReader.readResource(resource);
            //List<Document> enrichedDocuments = keywordEnricher.enrichDocuments(documentsToAdd);

        } catch (AssertionError e) {
            log.error("Error reading file {}", f.getFileName()
                                                .toString(), e);
        }
        return List.of();
    }

}
