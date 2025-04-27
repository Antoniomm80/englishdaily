package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.common.TimeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class MarkdownVocabularyLessonCreatedPrinterTest {
    private final TimeService timeService = Mockito.mock(TimeService.class);
    private final MarkdownVocabularyLessonCreatedPrinter printer = new MarkdownVocabularyLessonCreatedPrinter(timeService);

    @Test
    @DisplayName("Dado un evento con términos de vocabulario, debe generarse una impresión en Markdown")
    void givenVocabularyLessonCreatedEventShouldMarkdownPrintVersionBeGenerated() {
        given(timeService.now()).willReturn(LocalDate.of(2025, 4, 1));
        VocabularyLessonCreated vocabularyLessonCreated = new VocabularyLessonCreated(List.of(VocabularyTermMother.aVocabularyTerm()
                                                                                                                  .build(),
                VocabularyTermMother.aVocabularyTerm()
                                    .build()));
        String markdownText = printer.printEvent(vocabularyLessonCreated);

        assertThat(markdownText).isEqualTo("""
                *🇬🇧 Vocabulary lesson for 2025 04 01*
                
                
                *Term 1*
                *Source*: EWL B2 \\- 5\\.7 Vocabulary Lesson Script\\.pdf
                *Word*: to beaver away
                *Definition*: to work very hard at something for a long time
                *Part of Speech*: phrasal verb
                *Pronunciation*: /ˈbiː\\.vər əˈweɪ/
                *Example Sentence*: He was beavering away at his computer all night\\.
                *Collocations*: beaver away at something, beaver away on something
                *Synonyms*: to work hard, to toil
                
                
                *Term 2*
                *Source*: EWL B2 \\- 5\\.7 Vocabulary Lesson Script\\.pdf
                *Word*: to beaver away
                *Definition*: to work very hard at something for a long time
                *Part of Speech*: phrasal verb
                *Pronunciation*: /ˈbiː\\.vər əˈweɪ/
                *Example Sentence*: He was beavering away at his computer all night\\.
                *Collocations*: beaver away at something, beaver away on something
                *Synonyms*: to work hard, to toil
                """);
    }
}