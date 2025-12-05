package com.aspiratest.imaginaryonlinegame.exception.handler;

import com.aspiratest.imaginaryonlinegame.exception.InvalidExperienceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidExperienceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Map<String, String> handleInvalidExperienceException(InvalidExperienceException ex) {
    return Map.of("error", ex.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Map<String, String> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    return Map.of("error", ex.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
    return ex.getConstraintViolations().stream()
        .collect(
            Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return errors;
  }
}
