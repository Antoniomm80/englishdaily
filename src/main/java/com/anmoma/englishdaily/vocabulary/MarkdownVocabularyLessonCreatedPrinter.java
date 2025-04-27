package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.common.TimeService;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
class MarkdownVocabularyLessonCreatedPrinter implements VocabularyLessonCreatedPrinter {

    private final TimeService timeService;

    MarkdownVocabularyLessonCreatedPrinter(TimeService timeService) {
        this.timeService = timeService;
    }

    @Override
    public String printEvent(VocabularyLessonCreated event) {

        MarkdownMessageBuilder messageBuilder = new MarkdownMessageBuilder();
        messageBuilder.appendBold(String.format("\ud83c\uddec\ud83c\udde7 Vocabulary lesson for %s", timeService.now()
                                                                                                                .format(DateTimeFormatter.ofPattern(
                                                                                                                        "yyyy MM dd"))));
        int index = 1;
        messageBuilder.appendNewLine();
        for (VocabularyTerm term : event.vocabularyTerms()) {
            messageBuilder.appendNewLine();
            messageBuilder.appendNewLine();
            messageBuilder.appendBold(String.format("Term %s", index));
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Source");
            messageBuilder.append(term.source());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Word");
            messageBuilder.append(term.word());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Definition");
            messageBuilder.append(term.definition());
            messageBuilder.appendNewLine();
            if (term.partOfSpeech() != null && !term.partOfSpeech()
                                                    .isEmpty()) {
                messageBuilder.appendBoldForLabel("Part of Speech");
                messageBuilder.append(term.partOfSpeech());
                messageBuilder.appendNewLine();
            }
            if (term.pronunciation() != null && !term.pronunciation()
                                                     .isEmpty()) {
                messageBuilder.appendBoldForLabel("Pronunciation");
                messageBuilder.append(term.pronunciation());
                messageBuilder.appendNewLine();
            }
            if (term.exampleSentence() != null && !term.exampleSentence()
                                                       .isEmpty()) {
                messageBuilder.appendBoldForLabel("Example Sentence");
                messageBuilder.append(term.exampleSentence());
                messageBuilder.appendNewLine();
            }
            if (term.collocations() != null && !term.collocations()
                                                    .isEmpty()) {
                messageBuilder.appendBoldForLabel("Collocations");
                messageBuilder.append(String.join(", ", term.collocations()));
                messageBuilder.appendNewLine();
            }

            if (term.synonyms() != null && !term.synonyms()
                                                .isEmpty()) {
                messageBuilder.appendBoldForLabel("Synonyms");
                messageBuilder.append(String.join(", ", term.synonyms()));
                messageBuilder.appendNewLine();
            }
            index++;
        }
        return messageBuilder.build();
    }
}
