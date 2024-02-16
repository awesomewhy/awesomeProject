package com.dark.online.service.impl.security;

import com.dark.online.dto.jwt.AccessAndRefreshResponseDto;
import com.dark.online.dto.jwt.AccessResponseDto;
import com.dark.online.dto.mfa.MfaTokenData;
import com.dark.online.dto.jwt.JwtRequestDto;
import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.user.RegistrationUserDto;
import com.dark.online.entity.RefreshToken;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.AuthService;
import com.dark.online.service.TotpManagerService;
import com.dark.online.service.UserService;
import com.dark.online.util.JwtTokenUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String YOU_NEED_TO_RE_LOGIN = "you need to re-login";

    private final UserRepository userRepository;
    private final UserService userService;
    private final TotpManagerService totpManagerService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> login(@RequestBody JwtRequestDto authRequest) {
        Optional<User> userOptional = userRepository.findByNickname(authRequest.getNickname());
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "user not auth"));
        }
        if (StringUtils.isEmpty(authRequest.getNickname())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Nickname is required"));
        }
        if (StringUtils.isEmpty(authRequest.getPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Password is required"));
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "password or nickname incorrect"));
        }
        if (user.isAccountVerified()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.PERMANENT_REDIRECT.value(), "write code from google app")); // редирект для написания кода и гугл приложения если включена 2fa
        }
        ResponseEntity<?> response = getAccessToken(authRequest.getNickname(), authRequest.getPassword());
        if(response.getStatusCode().value() == (HttpStatus.OK.value())) {
            return getRefreshToken(authRequest.getNickname(), authRequest.getPassword());
        }
        return getAccessToken(authRequest.getNickname(), authRequest.getPassword());
    }

    @Override
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        if (userRepository.findByNickname(registrationUserDto.getNickname()).isPresent()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user with this nickname exists"));
        }
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "password did not match"));
        }
//        if(!Validation.isValidEmailAddress(registrationUserDto.getNickname())) {
//            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "INVALID_EMAIL"));
//        }
//        if(!Validation.isValidPassword(registrationUserDto.getPassword())) {
//            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "INVALID_PASSWORD"));
//        }
        userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "user register"));
//        return getAccessToken(registrationUserDto.getNickname(), registrationUserDto.getPassword()); если при регистрации надо будет токен выдовать
    }

    @Override
    @Transactional
    public ResponseEntity<?> create2FA() {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if (userOptional.isEmpty()) {
            ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not auth"));
        }
        User user = userOptional.get();
        user.setAccountVerified(true);
        userRepository.save(user);
        String qrCode;
        try {
            qrCode = totpManagerService.getQRCode(user.getSecretKey());
        } catch (QrGenerationException e) {
            return ResponseEntity.ok().body(new ErrorResponse
                    (HttpStatus.BAD_REQUEST.value(), "QrGenerationException in user service 78 line code"));
        }
        return ResponseEntity.ok()
                .body(MfaTokenData.builder()
                        .mfaCode(user.getSecretKey())
                        .qrCode(qrCode)
                        .build());
    }

    @Override
    public ResponseEntity<?> verifyCode(@RequestBody MfaVerificationRequest mfaVerificationRequest) {
        Optional<User> userOptional = userRepository.findByNickname(mfaVerificationRequest.getNickname());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isCodeValid = totpManagerService.verifyTotp(user.getSecretKey(), mfaVerificationRequest.getCode());
            if (isCodeValid) {
                return getAccessToken(user.getNickname(), passwordEncoder.encode(user.getPassword()));
            } else {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "incorrect code"));
            }
        } else {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user with this nickname not exists"));
        }
    }

    private ResponseEntity<?> getAccessToken(String nickname, String password) {
        try {
            authenticateUser(nickname, password);
            UserDetails userDetails = userService.loadUserByUsername(nickname);

            String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
            if (accessToken != null) {
                return ResponseEntity.ok(new AccessResponseDto(accessToken));
            } else {
                return ResponseEntity.ok(new ErrorResponse(HttpStatus.NOT_FOUND.value(), YOU_NEED_TO_RE_LOGIN));
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.ok()
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "INCORRECT_LOGIN_OR_PASSWORD"));
        }
    }

    private ResponseEntity<?> getRefreshToken(String nickname, String password) {
        try {
            authenticateUser(nickname, password);
            UserDetails userDetails = userService.loadUserByUsername(nickname);

            RefreshToken refreshToken = jwtTokenUtils.createRefreshToken(userDetails);
            return ResponseEntity.ok(new AccessAndRefreshResponseDto(getAccessToken(nickname, password).getBody().toString(),
                    String.valueOf(refreshToken.getToken())));

        } catch (BadCredentialsException e) {
            return ResponseEntity.ok()
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "INCORRECT_LOGIN_OR_PASSWORD"));
        }
    }

    private void authenticateUser(String nickname, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nickname, password));
    }

}
