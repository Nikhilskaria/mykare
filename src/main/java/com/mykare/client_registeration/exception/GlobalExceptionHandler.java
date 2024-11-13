package com.mykare.client_registeration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice

public class GlobalExceptionHandler {



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> UserAlreadyExists(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.CONFLICT.value()));
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.CONFLICT.value()));
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> Exception(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
