package com.ozon.online.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class UserNotAuthException extends Exception{
    private Integer code;
    private String message;
}
