package com.example.employeemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {
        
        String errorMessage = "Invalid request format";
        
        // Check if it's a date parsing error
        if (ex.getCause() instanceof DateTimeParseException) {
            errorMessage = "Invalid date format. Please use yyyy-MM-dd format (e.g., 2023-12-25)";
        } else if (ex.getMessage().contains("LocalDate")) {
            errorMessage = "Invalid date format for dateBirth. Please use yyyy-MM-dd format (e.g., 2023-12-25)";
        }
        
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(
            DateTimeParseException ex, WebRequest request) {
        return new ResponseEntity<>(
            "Invalid date format. Please use yyyy-MM-dd format (e.g., 2023-12-25)", 
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(
            Exception ex, WebRequest request) {
        ex.printStackTrace(); // Log the exception for debugging
        return new ResponseEntity<>(
            "An unexpected error occurred: " + ex.getMessage(), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
