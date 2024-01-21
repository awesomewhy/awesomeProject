package com.dark.online.service;

import com.dark.online.dto.jwt.JwtRequestDto;
import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.security.LoginRequestDto;
import com.dark.online.dto.user.RegistrationUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> login(@RequestBody JwtRequestDto authRequest);
    ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto);
    ResponseEntity<?> create2FA();
    ResponseEntity<?> verifyCode(@RequestBody MfaVerificationRequest mfaVerificationRequest);
}
