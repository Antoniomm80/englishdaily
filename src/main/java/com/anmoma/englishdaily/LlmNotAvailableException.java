package com.anmoma.englishdaily;

public class LlmNotAvailableException extends RuntimeException {
    public LlmNotAvailableException(String message) {
        super(message);
    }
}
