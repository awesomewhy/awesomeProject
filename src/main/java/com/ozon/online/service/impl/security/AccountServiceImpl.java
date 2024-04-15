package com.ozon.online.service.impl.security;

import com.ozon.online.dto.security.ChangePasswordDto;
import com.ozon.online.dto.user.ChangeNicknameDto;
import com.ozon.online.entity.User;
import com.ozon.online.entity.UserAvatar;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.mapper.ProductMapper;
import com.ozon.online.repository.UserAvatarRepository;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.service.AccountService;
import com.ozon.online.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleUnresolved;
import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAvatarRepository userAvatarRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) throws UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );
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
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "PASSWORD_CHANGED_SUCCESSFULLY"));
    }

    public ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameDto changeNicknameDto) throws UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );

        user.setNickname(changeNicknameDto.getNickname());

        userRepository.save(user);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "NICKNAME_CHANGED_SUCCESSFULLY"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile multipartFile) throws IOException, UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );

        if (user.getAvatarId() != null) {
            UserAvatar userAvatar = userAvatarRepository.findById(user.getAvatarId().getId()).orElseThrow(
                    () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "avatar not found")
            );
            productMapper.mapMultipartFileToUserAvatarAndSave(userAvatar, multipartFile);
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "avatar added"));
        }

        productMapper.saveUserAvatar(multipartFile, user);

        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "avatar added"));
    }
}
