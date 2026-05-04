package com.utkaldarshan.auth.common;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Map<String, Object>> handleApi(ApiException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("success", false);
    body.put("message", ex.getMessage());
    return ResponseEntity.status(ex.getStatus().value()).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("success", false);
    body.put("message", "Validation error");

    Map<String, String> errors = new HashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      errors.put(fe.getField(), fe.getDefaultMessage());
    }
    body.put("errors", errors);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Map<String, Object>> handleMethod(HttpRequestMethodNotSupportedException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("success", false);
    body.put("message", "Use POST for /login and /register");
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleOther(Exception ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("success", false);
    body.put("message", "Something went wrong");
    return ResponseEntity.internalServerError().body(body);
  }
}

