package com.dark.online.service.impl.security;

import com.dark.online.dto.jwt.JwtResponseDto;
import com.dark.online.dto.mfa.MfaTokenData;
import com.dark.online.dto.jwt.JwtRequestDto;
import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.user.RegistrationUserDto;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.AuthService;
import com.dark.online.service.TotpManagerService;
import com.dark.online.service.UserService;
import com.dark.online.util.JwtTokenUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final TotpManagerService totpManagerService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> login(@RequestBody JwtRequestDto authRequest) {
        if (StringUtils.isEmpty(authRequest.getNickname())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Nickname is required"));
        }
        if (StringUtils.isEmpty(authRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Password is required"));
        }
        Optional<User> userOptional = userRepository.findByNickname(authRequest.getNickname());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isAccountVerified()) {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.PERMANENT_REDIRECT.value(), "write code from google app"));
            }
        }
        return getAuthenticateUser(authRequest.getNickname(), authRequest.getPassword());
    }

    @Override
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "PASSWORDS_DID_NOT_MATCH"));
        }
        if (userRepository.findByNickname(registrationUserDto.getNickname()).isPresent()) {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "USER_WHIT_THIS_EMAIL_EXIST"));
        }
//        if(!Validation.isValidEmailAddress(registrationUserDto.getEmail())) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), INVALID_EMAIL), HttpStatus.BAD_REQUEST);
//        }
//        if(!Validation.isValidPassword(registrationUserDto.getPassword())) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), INVALID_PASSWORD), HttpStatus.BAD_REQUEST);
//        }
        userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "user register"));
//        return getAuthenticateUser(registrationUserDto.getNickname(), registrationUserDto.getPassword());
    }

    @Override
    public ResponseEntity<?> create2FA() {
        Optional<User> userOptional = userService.getAuthenticationPrincipalUserByNickname();
        if (userOptional.isEmpty()) {
            ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user in security context not found"));
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
    public ResponseEntity<?> verifyTopt(@RequestBody MfaVerificationRequest mfaVerificationRequest) {
        Optional<User> userOptional = userRepository.findByNickname(mfaVerificationRequest.getNickname());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isCodeValid = totpManagerService.verifyTotp(user.getSecretKey(), mfaVerificationRequest.getCode());
            if (isCodeValid) {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "user verified"));
            } else {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "user not verified"));
            }
        } else {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
        }
    }

    private ResponseEntity<?> getAuthenticateUser(String nickname, String password) {
        try {
            authenticateUser(nickname, password);
            UserDetails userDetails = userService.loadUserByUsername(nickname);
            String token = jwtTokenUtils.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok()
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "INCORRECT_LOGIN_OR_PASSWORD"));
        }
    }

    private void authenticateUser(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

}
