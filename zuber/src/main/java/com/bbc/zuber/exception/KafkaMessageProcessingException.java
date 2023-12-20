package com.bbc.zuber.exception;

public class KafkaMessageProcessingException extends RuntimeException {
    public KafkaMessageProcessingException() {
    }

    public KafkaMessageProcessingException(String message) {
        super(message);
    }
}
