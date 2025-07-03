package com.anmoma.englishdaily.catalog.grammarlesson;

public enum GrammarLessonLevel {
    B2(1), C1(2), ADVANCED_GRAMMAR_CHALLENGE(3);

    private int code;

    GrammarLessonLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static GrammarLessonLevel fromCode(int code) {
        for (GrammarLessonLevel s : GrammarLessonLevel.values()) {
            if (s.getCode() == code)
                return s;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
