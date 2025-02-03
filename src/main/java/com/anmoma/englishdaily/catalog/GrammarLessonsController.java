package com.anmoma.englishdaily.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/englishdaily")
class GrammarLessonsController {
    @GetMapping("grammar-lessons")
    ResponseEntity<GrammarLessonsResponse> findAllGrammarLessons() {
        List<GrammarLessonItem> grammarLessonItems = GrammarLesson.findAll()
                                                                  .stream()
                                                                  .map(lesson -> new GrammarLessonItem(lesson.name(), lesson.getTitle()))
                                                                  .toList();
        return ResponseEntity.ok(new GrammarLessonsResponse(grammarLessonItems));
    }

    record GrammarLessonItem(String title, String description) {
    }

    record GrammarLessonsResponse(List<GrammarLessonItem> lessons) {
    }
}
