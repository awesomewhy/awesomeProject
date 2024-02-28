package com.ozon.online.service;

import com.ozon.online.dto.security.ChangePasswordDto;
import com.ozon.online.dto.user.ChangeNicknameDto;
import com.ozon.online.exception.UserNotAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountService {
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) throws UserNotAuthException;
    ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameDto changeNicknameDto) throws UserNotAuthException;
}
