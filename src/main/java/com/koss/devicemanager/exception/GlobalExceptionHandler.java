package com.koss.devicemanager.exception;

import com.koss.devicemanager.dto.response.ExceptionResponseWrapper;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles PersistenceException and its subclasses.
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ExceptionResponseWrapper<Object>> handleEntityNotFoundException(PersistenceException ex) {
        log.error("PersistenceException occurred: {}", ex.getMessage(), ex);
        var response = new ExceptionResponseWrapper<>(null, HttpStatus.NOT_FOUND.value(), ex.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles MethodArgumentNotValidException for DTO validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseWrapper<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new TreeMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.warn("Validation failed: {}", errors);
        var response = new ExceptionResponseWrapper<>(errors, HttpStatus.BAD_REQUEST.value(), "Validation failed", false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any unexpected runtime exceptions.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseWrapper<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("RuntimeException occurred: {}", ex.getMessage(), ex);
        var response = new ExceptionResponseWrapper<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
