package com.dark.online.service;

import com.dark.online.dto.security.ChangePasswordDto;
import com.dark.online.dto.user.ChangeNicknameDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountService {
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto);
    ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameDto changeNicknameDto);
}
