package com.anmoma.englishdaily.catalog;

import java.util.List;

public interface CourseRepository {
    List<Course> findAll();

    List<Course> findAllVocabularySupportedCourses();
}
