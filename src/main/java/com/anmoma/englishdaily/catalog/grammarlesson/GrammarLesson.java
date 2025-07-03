package com.anmoma.englishdaily.catalog.grammarlesson;

import jakarta.persistence.*;

@Entity
@Table(name = "grammar_lessons")
public class GrammarLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private GrammarLessonLevel level;

    public static GrammarLesson grammarLessonWithTitleAndLevel(String title, GrammarLessonLevel level) {
        GrammarLesson lesson = new GrammarLesson();
        lesson.title = title;
        lesson.level = level;
        return lesson;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public GrammarLessonLevel getLevel() {
        return level;
    }
}
