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

    IngestionPipeline(VectorStore vectorStore, FolderReader folderReader, KeywordEnricher keywordEnricher, DocumentReader documentReader,
            CourseRepository courseRepository) {
        this.vectorStore = vectorStore;
        this.folderReader = folderReader;
        this.keywordEnricher = keywordEnricher;
        this.documentReader = documentReader;
        this.courseRepository = courseRepository;
    }

    public void populateVectorStore() {
        List<Course> allCourses = courseRepository.findAll();
        allCourses.stream()
                  .map(Course::getFolderPath)
                  .forEach(folder -> {
                      List<Path> filesList = folderReader.getFilenamesFromFolder("documents/" + folder);
                      filesList.forEach(f -> {
                          log.info("Reading file {}", f.getFileName()
                                                       .toString());
                          Resource resource = new PathResource(f);
                          List<Document> documentsToAdd = documentReader.readResource(resource);
                          //List<Document> enrichedDocuments = keywordEnricher.enrichDocuments(documentsToAdd);
                          vectorStore.add(documentsToAdd);
                      });
                  });
    }

}
