package com.anmoma.englishdaily.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/englishdaily")
class GrammarLessonsController {
    private final GrammarLessonRepository grammarLessonRepository;

    GrammarLessonsController(GrammarLessonRepository grammarLessonRepository) {
        this.grammarLessonRepository = grammarLessonRepository;
    }

    @GetMapping("grammar-lessons/{levelId}")
    ResponseEntity<GrammarLessonsResponse> findGrammarLessonsByLevel(@PathVariable Integer levelId) {
        List<GrammarLessonItem> lessons = grammarLessonRepository.findGrammarLessonsByLevel(GrammarLessonLevel.fromCode(levelId))
                                                                 .stream()
                                                                 .map(lesson -> new GrammarLessonItem(lesson.getId(), lesson.getTitle()))
                                                                 .toList();

        return ResponseEntity.ok(new GrammarLessonsResponse(lessons));
    }

    record GrammarLessonItem(Long id, String title) {
    }

    record GrammarLessonsResponse(List<GrammarLessonItem> lessons) {
    }
}
