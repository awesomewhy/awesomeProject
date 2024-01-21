package com.dark.online.service.impl.security;

import com.dark.online.dto.security.ChangePasswordDto;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.AccountService;
import com.dark.online.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        Optional<User> updateUser = userService.getAuthenticationPrincipalUserByNickname();
        if (updateUser.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "USER_NOT_FOUND"));
        }
        User user = updateUser.get();

        if (StringUtils.isEmpty(changePasswordDto.getOldPassword())
                || StringUtils.isEmpty(changePasswordDto.getNewPassword())
                || StringUtils.isEmpty(changePasswordDto.getRepeatPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "PASSWORD_NULL"));
        }
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "OLD_PASSWORD_NOT_MATCH"));
        }
        if (passwordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "PASSWORD_MATCHED"));
        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "BAD_REPEAT_PASSWORD"));
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "PASSWORD_CHANGED_SUCCESSFULLY"));
    }
}
