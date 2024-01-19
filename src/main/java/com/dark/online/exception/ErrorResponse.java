package com.dark.online.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        HttpStatus code,
        String message)
{

}
