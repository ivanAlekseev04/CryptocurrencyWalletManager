package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<Map<String, String>> handleFailedAuthentication(BadCredentialsException e) {
        var msg = Map.of("message", e.getMessage());

        log.error(msg.toString());
        return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
    }

    // catch internal server errors
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Map<String, String>> handleInternalServerErrors(IllegalArgumentException e) {
        var msg = Map.of("message", e.getMessage());

        log.error(msg.toString());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    // catch DB exceptions
    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Map<String, String>> handleNotFoundDBException(EntityNotFoundException e) {
        var msg = Map.of("message", e.getMessage());

        log.error(msg.toString());
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Map<String, String>> handleInsertingDuplicatesToDB(DataIntegrityViolationException e) {
        Map<String, String> errors = new HashMap<>();
        Arrays.stream(e.getMostSpecificCause().getMessage().split("\n"))
                .map(String::strip).forEach(errLine -> {
                    String[] err = errLine.split(":");
                    errors.put(err[0], err[1]);
                });

        log.error(errors.toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // catch Hibernate validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Map<String, String>> handleArgumentValidationException(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        FieldError::getDefaultMessage, (o1, o2) -> o1, HashMap::new));

        log.error(errors.toString());
        return ResponseEntity.badRequest().body(errors);
    }
}
