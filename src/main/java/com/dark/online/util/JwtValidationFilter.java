package com.dark.online.util;

import com.dark.online.service.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtValidationFilter {
    private JWTService jwtService;
}
