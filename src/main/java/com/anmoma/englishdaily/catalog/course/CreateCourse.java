package com.anmoma.englishdaily.catalog.course;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCourse {
    private final CourseRepository courseRepository;

    public CreateCourse(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    void create(CreateCourseCommand command) {
        Course course = command.vocabularySupported() ?
                Course.withTitleFolderPathAndVocabularySupported(command.title(), command.folderPath()) :
                Course.withTitleAndFolderPath(command.title(), command.folderPath());
        courseRepository.create(course);
    }
}
