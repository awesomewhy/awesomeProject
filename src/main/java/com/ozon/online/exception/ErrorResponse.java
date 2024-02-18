package com.ozon.online.exception;

public record ErrorResponse(
        Integer code,
        String message)
{

}
