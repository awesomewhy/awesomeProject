package com.ozon.online.service;

import com.ozon.online.dto.security.ChangePasswordDto;
import com.ozon.online.dto.user.ChangeNicknameDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountService {
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto);
    ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameDto changeNicknameDto);
}
