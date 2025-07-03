package com.anmoma.englishdaily.catalog.course;

import java.util.List;

public interface CourseRepository {
    void create(Course course);

    List<Course> findAll();

    List<Course> findAllVocabularySupportedCourses();
}
