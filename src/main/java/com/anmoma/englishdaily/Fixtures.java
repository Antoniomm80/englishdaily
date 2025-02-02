package com.anmoma.englishdaily;

import com.anmoma.englishdaily.catalog.Course;

import java.util.List;

public class Fixtures {
    private static List<Course> courses;

    static {
        Course agc = Course.withTitleFolderPath("Advanced Grammar Challenge", "Advanced Grammar Challenge");
        Course pvc = Course.withTitleFolderPathAndVocabularySupported("Phrasal Verbs Challenge", "phrasalverbschallenge");
        Course sc = Course.withTitleFolderPathAndVocabularySupported("Slang Challenge", "slangchallenge");
        courses = List.of(agc, pvc, sc);
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static List<Course> getVocabularySupportedCourses() {
        return courses.stream()
                      .filter(Course::isVocabularySupported)
                      .toList();
    }
}
