package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.common.TimeService;
import org.springframework.stereotype.Component;

@Component
class MarkdownVocabularyLessonCreatedPrinter implements VocabularyLessonCreatedPrinter {

    private final TimeService timeService;

    MarkdownVocabularyLessonCreatedPrinter(TimeService timeService) {
        this.timeService = timeService;
    }

    @Override
    public String printEvent(VocabularyLessonCreated event) {

        MarkdownMessageBuilder messageBuilder = new MarkdownMessageBuilder();
        messageBuilder.appendHeader1(String.format("\ud83c\uddec\ud83c\udde7 This is the vocabulary lesson for %s", timeService.now()));
        int index = 1;
        for (VocabularyTerm term : event.vocabularyTerms()) {
            messageBuilder.appendHeader2(String.format("Term %s", index));
            messageBuilder.appendBoldForLabel("Source");
            messageBuilder.append(term.source());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Word");
            messageBuilder.append(term.word());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Definition");
            messageBuilder.append(term.definition());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Part of Speech");
            messageBuilder.append(term.partOfSpeech());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Pronunciation");
            messageBuilder.append(term.pronunciation());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Example Sentence");
            messageBuilder.append(term.exampleSentence());
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Collocations");
            messageBuilder.append(String.join(", ", term.collocations()));
            messageBuilder.appendNewLine();
            messageBuilder.appendBoldForLabel("Synonyms");
            messageBuilder.append(String.join(", ", term.synonyms()));
            messageBuilder.appendNewLine();
            messageBuilder.appendHorizontalRule();
            index++;
        }
        return messageBuilder.build();
    }
}
