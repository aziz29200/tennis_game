package com.tennis.scoring.adapter.web;

import com.tennis.scoring.domain.exception.GameFinishedException;
import com.tennis.scoring.domain.exception.InvalidSequenceException;
import com.tennis.scoring.domain.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSequenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSequence(
            InvalidSequenceException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid sequence",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(GameFinishedException.class)
    public ResponseEntity<ErrorResponse> handleGameFinished(
            GameFinishedException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Game already finished",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.badRequest().body(body);
    }
}
