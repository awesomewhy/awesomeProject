package com.dark.online.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@CrossOrigin(origins = "*")
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        String errorMessage = ex.getMessage();
        String errorType = ex.getClass().getSimpleName();
        return ResponseEntity.ok().body("some problem with server\n" + errorType + "\n" + errorMessage);
    }
}
