package com.anmoma.englishdaily.catalog.course;

public record CreateCourseCommand(String title, String folderPath, boolean vocabularySupported) {
}
