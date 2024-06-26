package com.ozon.online.service.impl.security;

import com.ozon.online.dto.jwt.AccessAndRefreshResponseDto;
import com.ozon.online.dto.jwt.AccessResponseDto;
import com.ozon.online.dto.jwt.JwtRequestDto;
import com.ozon.online.dto.mfa.MfaTokenData;
import com.ozon.online.dto.mfa.MfaVerificationRequest;
import com.ozon.online.dto.user.RegistrationUserDto;
import com.ozon.online.entity.RefreshToken;
import com.ozon.online.entity.User;
import com.ozon.online.exception.ErrorResponse;
import com.ozon.online.exception.UserNotAuthException;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.service.AuthService;
import com.ozon.online.service.TotpManagerService;
import com.ozon.online.service.UserService;
import com.ozon.online.util.JwtTokenUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

import javax.print.attribute.HashAttributeSet;

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
    public ResponseEntity<?> login(@Valid @RequestBody JwtRequestDto authRequest) throws UserNotAuthException {
        User user = userRepository.findByNickname(authRequest.getNickname()).orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not found")
        );

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "password or nickname incorrect"));
        }
        if (user.isAccountVerified()) {
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).body(new ErrorResponse(HttpStatus.PERMANENT_REDIRECT.value(), "write code from google app")); // redirect for writing code and google applications if 2fa is enabled
        }
        ResponseEntity<?> response = getAccessToken(authRequest.getNickname(), authRequest.getPassword());
        if (response.getStatusCode().value() == HttpStatus.BAD_REQUEST.value()) {
            return getRefreshToken(authRequest.getNickname(), authRequest.getPassword());
        }
        return getAccessToken(authRequest.getNickname(), authRequest.getPassword());
    }

    @Override
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        if (userRepository.findByNickname(registrationUserDto.getNickname()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user with this nickname exists"));
        }
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "password did not match"));
        }
//        if(!Validation.isValidEmailAddress(registrationUserDto.getNickname())) {
//            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.OK.value(), "INVALID_EMAIL"));
//        }
//        if(!Validation.isValidPassword(registrationUserDto.getPassword())) {
//            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.OK.value(), "INVALID_PASSWORD"));
//        }
        userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "user register"));
//        return getAccessToken(registrationUserDto.getNickname(), registrationUserDto.getPassword()); если при регистрации надо будет токен выдавать / if during registration you need to issue a token
    }

    @Override
    @Transactional
    public ResponseEntity<?> create2FA() throws QrGenerationException, UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );
        user.setAccountVerified(true);
        userRepository.save(user);
        String qrCode = totpManagerService.getQRCode(user.getSecretKey());

        return ResponseEntity.ok()
                .body(MfaTokenData.builder()
                        .mfaCode(user.getSecretKey())
                        .qrCode(qrCode)
                        .build());
    }

    @Override
    public ResponseEntity<?> verifyCode(@RequestBody MfaVerificationRequest mfaVerificationRequest) throws UserNotAuthException {
        User user = userService.getAuthenticationPrincipalUserByNickname().orElseThrow(
                () -> new UserNotAuthException(HttpStatus.NOT_FOUND.value(), "user not auth")
        );

        boolean isCodeValid = totpManagerService.verifyTotp(user.getSecretKey(), mfaVerificationRequest.getCode());
        if (isCodeValid) {
            return getAccessToken(user.getNickname(), passwordEncoder.encode(user.getPassword()));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "incorrect code"));
        }

    }

    private ResponseEntity<?> getAccessToken(String nickname, String password) throws BadCredentialsException {
        authenticateUser(nickname, password);
        UserDetails userDetails = userService.loadUserByUsername(nickname);

        String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
        if (accessToken != null) {
            return ResponseEntity.ok(new AccessResponseDto(accessToken));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), YOU_NEED_TO_RE_LOGIN));
        }
    }

    private ResponseEntity<?> getRefreshToken(String nickname, String password) throws BadCredentialsException {
        authenticateUser(nickname, password);
        UserDetails userDetails = userService.loadUserByUsername(nickname);

        RefreshToken refreshToken = jwtTokenUtils.createRefreshToken(userDetails);
        return ResponseEntity.ok(new AccessAndRefreshResponseDto(jwtTokenUtils.generateAccessToken(userDetails),
                String.valueOf(refreshToken.getToken())));
    }

    private void authenticateUser(String nickname, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nickname, password));
    }

}
