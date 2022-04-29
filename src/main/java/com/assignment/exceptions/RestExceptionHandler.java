package com.assignment.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler({FavoriteCoinException.class, CoinException.class})
  public ResponseEntity<ErrorResponse> notFound(RuntimeException e) {
    log.error(e.getClass().getName(), e);
    return create(e, NOT_FOUND);
  }

  private ResponseEntity<ErrorResponse> create(Exception e, HttpStatus httpStatus) {
    var errorResponse = new ErrorResponse(
        httpStatus.value(),
        httpStatus.getReasonPhrase(),
        Instant.now(),
        e.getLocalizedMessage()
    );
    return ResponseEntity.status(httpStatus).body(errorResponse);
  }

  record ErrorResponse(int code, String status, Instant timestamp, String message) {

  }
}
