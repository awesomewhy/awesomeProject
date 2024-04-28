package com.ozon.online.exception;

public class ErrorResponse {
    Integer code;
    String message;

    public  ErrorResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}