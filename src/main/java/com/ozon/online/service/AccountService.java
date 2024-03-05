package com.ozon.online.service;

import com.ozon.online.dto.security.ChangePasswordDto;
import com.ozon.online.dto.user.ChangeNicknameDto;
import com.ozon.online.exception.UserNotAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AccountService {
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) throws UserNotAuthException;
    ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameDto changeNicknameDto) throws UserNotAuthException;
    ResponseEntity<?> addImage(@RequestParam("image") MultipartFile multipartFile) throws IOException, UserNotAuthException;
}
