package com.tennis.scoring.domain.exception;

public class InvalidSequenceException  extends RuntimeException {
    public InvalidSequenceException(String message) {
        super(message);
    }
}
