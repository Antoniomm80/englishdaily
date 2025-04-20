package com.anmoma.englishdaily.vocabulary;

import org.springframework.stereotype.Component;

@Component
class PayloadPrinter {
    public void printPayload(String payload) {
        System.out.println("Received payload: " + payload);
    }
}
