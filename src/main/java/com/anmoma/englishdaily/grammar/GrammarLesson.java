package com.anmoma.englishdaily.grammar;

public enum GrammarLesson {
    //@formatter:off
    ADVANCED_COMPARATIVE_STRUCTURES("Advanced Comparative Structures"),
    GRADABLE_AND_NON_GRADABLE_ADJECTIVES("Gradable and Non-gradable Adjectives"),
    PAST_AND_PRESENTS_ASSUMPTIONS("Past and Presents Assumptions"),
    SPECULATION_AND_DEDUCTION("Speculation and Deduction"),
    MIXED_CONDITIONALS("Mixed Conditionals"),
    THE_SUBJUNCTIVE_MOOD("The Subjunctive Mood"),
    TRANSITIVE_AND_INTRASITIVE_VERBS("Transitive and Intransitive Verbs"),
    VERB_PATTERNS("Verb Patterns"),
    //TODO: Falta el 10
    RELATIVE_AND_PARTICIPLE_CLAUSES("Relative and Participle Clauses"),
    USED_TO_GET_USED_TO_BE_USED_TO("Used to, Get Used to, Be Used to"),
    IMPERSONAL_PASSIVE_VOICE("Impersonal Passive Voice, Get/Have Structures"),
    CLEFT_SENTENCES("Cleft Sentences");
    //TODO: Falta el 15

    // @formatter:on
    private final String title;

    GrammarLesson(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
