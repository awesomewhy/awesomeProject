package com.dark.online.service;

import com.dark.online.dto.jwt.JwtRequestDto;
import com.dark.online.dto.security.LoginRequestDto;
import com.dark.online.dto.user.RegistrationUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDto authRequest);
    ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto);
    ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest);
    ResponseEntity<?> create2FA();

}
