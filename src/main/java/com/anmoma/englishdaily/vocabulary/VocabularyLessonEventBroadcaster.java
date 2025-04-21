package com.anmoma.englishdaily.vocabulary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class VocabularyLessonEventBroadcaster {
    private static final Logger log = LoggerFactory.getLogger(VocabularyLessonEventBroadcaster.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final ObjectMapper objectMapper;
    private final VocabularyLessonCreatedPrinter vocabularyLessonCreatedPrinter;

    public VocabularyLessonEventBroadcaster(RabbitTemplate rabbitTemplate, @Value("${com.antoniomm.englishdaily.exchange-name}") String exchangeName,
            ObjectMapper objectMapper, VocabularyLessonCreatedPrinter vocabularyLessonCreatedPrinter) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.objectMapper = objectMapper;
        this.vocabularyLessonCreatedPrinter = vocabularyLessonCreatedPrinter;
    }

    @EventListener
    public void handleRecipeCreatedEvent(VocabularyLessonCreated event) throws JsonProcessingException {
        log.info("Enviando evento de vocabulario creada al exchange {}", exchangeName);
        EventMessage<String> message = new EventMessage<>(Instant.now(), vocabularyLessonCreatedPrinter.printEvent(event), true);
        rabbitTemplate.convertAndSend(exchangeName, "", objectMapper.writeValueAsString(message));
    }
}