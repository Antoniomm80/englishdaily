package com.anmoma.englishdaily.documentreader;

import com.anmoma.englishdaily.catalog.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilenameProvider {
    private final FolderReader folderReader;
    private final CourseRepository courseRepository;

    public FilenameProvider(FolderReader folderReader, CourseRepository courseRepository) {
        this.folderReader = folderReader;
        this.courseRepository = courseRepository;
    }

    public String getRandomFilenameFromDocumentsFolder() {
        List<String> documents = courseRepository.findAllVocabularySupportedCourses()
                                                 .stream()
                                                 .flatMap(course -> folderReader.getFilenamesFromFolder(course.getFolderPath())
                                                                                .stream())
                                                 .toList();
        return documents.get((int) (Math.random() * documents.size()));
    }

}
