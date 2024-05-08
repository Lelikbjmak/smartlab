package com.innowise.smartlab.controller.handler;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    log.debug("Exception MethodArgumentNotValidException has occurred. Message: `{}`",
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
