package com.anmoma.englishdaily.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/englishdaily")
class GrammarLessonsController {
    private final GrammarLessonRepository grammarLessonRepository;
    private final CreateGrammarLesson createGrammarLesson;

    GrammarLessonsController(GrammarLessonRepository grammarLessonRepository, CreateGrammarLesson createGrammarLesson) {
        this.grammarLessonRepository = grammarLessonRepository;
        this.createGrammarLesson = createGrammarLesson;
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

    @PostMapping("grammar-lessons")
    ResponseEntity<Void> createGrammarLesson(@RequestBody CreateGrammarLessonRequest request) {
        createGrammarLesson.create(new CreateGrammarLessonCommand(request.title(), request.level()));
        return ResponseEntity.ok()
                             .build();
    }

    record CreateGrammarLessonRequest(String title, GrammarLessonLevel level) {
    }
}
