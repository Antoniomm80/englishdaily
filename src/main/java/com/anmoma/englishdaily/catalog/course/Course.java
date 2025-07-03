package com.anmoma.englishdaily.catalog.course;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String folderPath;
    private boolean vocabularySupported;

    public static Course withTitleAndFolderPath(String title, String folderPath) {
        Course course = new Course();
        course.title = title;
        course.folderPath = folderPath;
        course.vocabularySupported = false;
        return course;
    }

    public static Course withTitleFolderPathAndVocabularySupported(String title, String folderPath) {
        Course course = new Course();
        course.title = title;
        course.folderPath = folderPath;
        course.vocabularySupported = true;
        return course;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public boolean isVocabularySupported() {
        return vocabularySupported;
    }
}
