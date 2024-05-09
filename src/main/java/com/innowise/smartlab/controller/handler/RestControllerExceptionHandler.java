package com.innowise.smartlab.controller.handler;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    logger.debug("Exception MethodArgumentNotValidException has occurred. Message: `{}`",
        exception.getMessage());
    Map<String, Object> errors = extractConstraintsViolations(exception);

    return ResponseEntity.badRequest().body(errors);
  }

  private Map<String, Object> extractConstraintsViolations(
      MethodArgumentNotValidException exception) {
    var errors = new HashMap<String, Object>();
    exception.getBindingResult().getAllErrors().forEach(error -> {
      String errorPath = error instanceof FieldError fieldError ? fieldError.getField() :
          error.getObjectName();
      String errorMessage = error.getDefaultMessage();
      errors.put(errorPath, errorMessage);
    });
    return errors;
  }
}
