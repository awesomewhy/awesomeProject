package com.ozon.online.controller.advice;

import com.ozon.online.exception.UserNotAuthException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@CrossOrigin(origins = "http://localhost:3000")
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        String errorMessage = ex.getMessage();
        String errorType = ex.getClass().getSimpleName();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("some problem with server \n" + errorType + "\n" + errorMessage);
    }

    @ExceptionHandler(UserNotAuthException.class)
    public ResponseEntity<?> handleException(UserNotAuthException ex) {
        String errorMessage = ex.getMessage();
        String errorType = ex.getClass().getSimpleName();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not auth \n" + errorType + "\n" + errorMessage);
    }

    protected ResponseEntity<?> handleExc(MethodArgumentNotValidException ex, HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {
        Map<String, Object> qwe = new LinkedHashMap<>();
        qwe.put("timestamp", System.currentTimeMillis());
        qwe.put("status", httpStatus.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        qwe.put("errors", errors);

        return new ResponseEntity<>(qwe, httpStatus);
    }
}
