package com.ozon.online.controller.security;

import com.ozon.online.dto.security.ChangePasswordDto;
import com.ozon.online.dto.user.ChangeNicknameDto;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.service.AccountService;
import com.ozon.online.service.AuthService;
import dev.samstevens.totp.exceptions.QrGenerationException;
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
    public ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameDto changeNicknameDto) throws UserNotAuthException {
        return accountService.changeNickname(changeNicknameDto);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> verifyTotp(@RequestBody ChangePasswordDto changePasswordDto) throws UserNotAuthException {
        return accountService.changePassword(changePasswordDto);
    }

    @PostMapping("/create2FA")
    public ResponseEntity<?> create2FA() throws QrGenerationException, UserNotAuthException {
        return authService.create2FA();
    }
}
