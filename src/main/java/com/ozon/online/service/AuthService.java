package com.ozon.online.service;

import com.ozon.online.dto.jwt.JwtRequestDto;
import com.ozon.online.dto.mfa.MfaVerificationRequest;
import com.ozon.online.dto.user.RegistrationUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> login(@RequestBody JwtRequestDto authRequest);
    ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto);
    ResponseEntity<?> create2FA();
    ResponseEntity<?> verifyCode(@RequestBody MfaVerificationRequest mfaVerificationRequest);
}
