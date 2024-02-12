package com.dark.online.controller.security;

import com.dark.online.dto.jwt.JwtRequestDto;
import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.security.ChangePasswordDto;
import com.dark.online.dto.user.ChangeNicknameDto;
import com.dark.online.dto.user.RegistrationUserDto;
import com.dark.online.service.AccountService;
import com.dark.online.service.AuthService;
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
    public ResponseEntity<?> login(@RequestBody JwtRequestDto loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/verifycode")
    public ResponseEntity<?> verifyTotp(@RequestBody MfaVerificationRequest mfaVerificationRequest) {
        return authService.verifyCode(mfaVerificationRequest);
    }
}
