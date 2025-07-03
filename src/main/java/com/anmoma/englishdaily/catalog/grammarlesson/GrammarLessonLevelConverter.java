package com.anmoma.englishdaily.catalog.grammarlesson;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GrammarLessonLevelConverter implements AttributeConverter<GrammarLessonLevel, Integer> {
    @Override
    public Integer convertToDatabaseColumn(GrammarLessonLevel grammarLessonLevel) {
        return grammarLessonLevel != null ? grammarLessonLevel.getCode() : null;
    }

    @Override
    public GrammarLessonLevel convertToEntityAttribute(Integer code) {
        return code != null ? GrammarLessonLevel.fromCode(code) : null;
    }
}
