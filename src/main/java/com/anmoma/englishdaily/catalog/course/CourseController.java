package com.anmoma.englishdaily.catalog.course;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/englishdaily")
class CourseController {
    private final CreateCourse createCourse;

    CourseController(CreateCourse createCourse) {
        this.createCourse = createCourse;
    }

    @PostMapping("courses")
    ResponseEntity<Void> createCourse(@RequestBody CreateCourseRequest request) {
        createCourse.create(new CreateCourseCommand(request.title(), request.folderPath(), request.vocabularySupported()));
        return ResponseEntity.ok()
                             .build();
    }

    record CreateCourseRequest(String title, String folderPath, boolean vocabularySupported) {
    }
}