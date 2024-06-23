package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Map<String, String>> handleUnauthorizedUser(UsernameNotFoundException e) {
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
    @ExceptionHandler(InsufficientFundsException.class)
    public final ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException e) {
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
        var msg = Map.of("message", e.getMessage());

        log.error(msg.toString());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
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
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        //var errorMessage = Map.of(ex.getConstraintName(), ex.getErrorMessage());
        var errorMessage = Map.of("message", ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getMessage())
                .findFirst()
                .orElse("Validation error"));

        log.error(errorMessage.toString());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestToAPIException.class)
    public final ResponseEntity<Map<String, String>> handleBadRequestToAPI(BadRequestToAPIException e) {
        var msg = Map.of("message", e.getMessage());

        log.error(msg.toString());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AssetNotFoundException.class)
    public final ResponseEntity<Map<String, String>> handleAssetNotFoundException(AssetNotFoundException e) {
        var msg = Map.of("message", e.getMessage());

        log.error(msg.toString());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }
}
