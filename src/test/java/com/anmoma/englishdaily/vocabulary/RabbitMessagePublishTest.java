package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teketik.test.mockinbean.MockInBean;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

@IntegrationTest
@Testcontainers
class RabbitMessagePublishTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${com.antoniomm.englishdaily.exchange-name}")
    private String exchange;

    @MockInBean(TestQueueListener.class)
    private PayloadPrinter payloadPrinter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Se debe poder enviar mensajes al exchange configurado")
    void shouldBeAbleToPublishMessage() {
        EventMessage<String> message = new EventMessage<>(Instant.now(), "Este es un mensaje de prueba", false);
        Assertions.assertThatCode(() -> rabbitTemplate.convertAndSend(exchange, "test", objectMapper.writeValueAsString(message)))
                  .doesNotThrowAnyException();

        await().atMost(5, SECONDS)
               .untilAsserted(() -> then(payloadPrinter).should(atLeastOnce())
                                                        .printPayload("Este es un mensaje de prueba"));
    }
}