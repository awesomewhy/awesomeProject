package com.dark.online.controller.security;

import com.dark.online.dto.security.ChangePasswordDto;
import com.dark.online.dto.user.ChangeNicknameDto;
import com.dark.online.service.AccountService;
import com.dark.online.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/safety")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    private final AccountService accountService;
    private final AuthService authService;

    @GetMapping("/changenickname")
    public ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameDto changeNicknameDto) {
        return accountService.changeNickname(changeNicknameDto);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> verifyTotp(@RequestBody ChangePasswordDto changePasswordDto) {
        return accountService.changePassword(changePasswordDto);
    }

    @PostMapping("/create2FA")
    public ResponseEntity<?> create2FA() {
        return authService.create2FA();
    }
}
