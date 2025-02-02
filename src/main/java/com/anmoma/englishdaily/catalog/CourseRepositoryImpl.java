package com.anmoma.englishdaily.catalog;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepository {
    private final CourseJpaRepository courseJpaRepository;

    public CourseRepositoryImpl(CourseJpaRepository courseJpaRepository) {
        this.courseJpaRepository = courseJpaRepository;
    }

    @Override
    public List<Course> findAll() {
        return courseJpaRepository.findAll();
    }

    @Override
    public List<Course> findAllVocabularySupportedCourses() {
        return courseJpaRepository.findAllByVocabularySupportedTrue();
    }
}
