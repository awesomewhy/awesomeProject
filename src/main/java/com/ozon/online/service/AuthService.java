package com.ozon.online.service;

import com.ozon.online.dto.jwt.JwtRequestDto;
import com.ozon.online.dto.mfa.MfaVerificationRequest;
import com.ozon.online.dto.user.RegistrationUserDto;
import com.ozon.online.exception.UserNotAuthException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> login(@RequestBody JwtRequestDto authRequest) throws UserNotAuthException;
    ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto);
    ResponseEntity<?> create2FA() throws QrGenerationException, UserNotAuthException;
    ResponseEntity<?> verifyCode(@RequestBody MfaVerificationRequest mfaVerificationRequest) throws UserNotAuthException;
}
