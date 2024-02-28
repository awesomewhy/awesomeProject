package com.ozon.online.controller.security;

import com.ozon.online.dto.jwt.JwtRequestDto;
import com.ozon.online.dto.mfa.MfaVerificationRequest;
import com.ozon.online.dto.user.RegistrationUserDto;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.service.AccountService;
import com.ozon.online.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//data:image/jpeg;base64,
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AccountService accountService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.register(registrationUserDto);
    }

    @PostMapping( "/login")
    public ResponseEntity<?> login(@RequestBody JwtRequestDto loginRequest) throws UserNotAuthException {
        return authService.login(loginRequest);
    }

    @PostMapping("/verifycode")
    public ResponseEntity<?> verifyTotp(@RequestBody MfaVerificationRequest mfaVerificationRequest) throws UserNotAuthException {
        return authService.verifyCode(mfaVerificationRequest);
    }
}
