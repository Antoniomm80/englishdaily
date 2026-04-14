package com.anmoma.englishdaily.documentreader;

import com.anmoma.englishdaily.catalog.course.CourseRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class FilenameProvider {
    private final FolderReader folderReader;
    private final CourseRepository courseRepository;
    private static final SecureRandom random = new SecureRandom();

    public FilenameProvider(FolderReader folderReader, CourseRepository courseRepository) {
        this.folderReader = folderReader;
        this.courseRepository = courseRepository;
    }

    public List<SelectedDocument> getAllSelectedDocuments() {
        List<SelectedDocument> documents = courseRepository.findAllVocabularySupportedCourses()
                                                           .stream()
                                                           .flatMap(course -> folderReader.getFilenamesFromFolder(course.getFolderPath())
                                                                                          .stream()
                                                                                          .map(filename -> new SelectedDocument(filename, course.getFolderPath())))
                                                           .toList();
        if (documents.isEmpty()) {
            throw new IllegalStateException("No vocabulary documents available across all supported courses");
        }
        return documents;
    }

    public SelectedDocument getRandomSelectedDocument() {
        List<SelectedDocument> documents = getAllSelectedDocuments();
        return documents.get(random.nextInt(documents.size()));
    }

    public String getRandomFilenameFromDocumentsFolder() {
        return getRandomSelectedDocument().filename();
    }

}
