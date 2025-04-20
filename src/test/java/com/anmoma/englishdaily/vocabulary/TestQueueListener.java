package com.anmoma.englishdaily.vocabulary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestQueueListener {
    private final PayloadPrinter payloadPrinter;
    private final ObjectMapper objectMapper;

    public TestQueueListener(PayloadPrinter payloadPrinter, ObjectMapper objectMapper) {
        this.payloadPrinter = payloadPrinter;

        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "englishdaily.home.events")
    public void handle(final Message payload) throws IOException {
        String body = new String(payload.getBody(), java.nio.charset.StandardCharsets.UTF_8);
        EventMessage<String> eventMessage = objectMapper.readValue(body, EventMessage.class);
        payloadPrinter.printPayload(eventMessage.message());
    }
}
