package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.IntegrationTest;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

@IntegrationTest
class VocabularyLessonCreationEventTest {

    @Autowired
    private VocabularyLessonCreator vocabularyLessonCreator;

    @MockInBean(VocabularyLessonEventBroadcaster.class)
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("When a vocabulary lesson is created, a message should be sent to the RabbitMQ exchange")
    void shouldSendMessageToRabbitMQWhenVocabularyLessonIsCreated() {
        // Given
        vocabularyLessonCreator.generateVocabularyLesson();
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        then(rabbitTemplate).should()
                            .convertAndSend(eq("exchange.home.events"), eq(""), messageCaptor.capture());

        assertThat(messageCaptor.getValue()).contains("""
                🇬🇧 Vocabulary lesson for""");
    }
}