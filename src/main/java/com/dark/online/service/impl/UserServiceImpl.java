package com.dark.online.service.impl;

import com.dark.online.dto.mfa.MfaTokenData;
import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.security.LoginRequestDto;
import com.dark.online.dto.user.RegistrationUserDto;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.TotpManagerService;
import com.dark.online.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpManagerService totpManager;

    @Override
    public ResponseEntity<?> verifyTopt(@RequestBody MfaVerificationRequest mfaVerificationRequest) {
        Optional<User> userOptional = userRepository.findByNickname(mfaVerificationRequest.getNickname());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isCodeValid = totpManager.verifyTotp(user.getSecretKey(), mfaVerificationRequest.getCode());
            if (isCodeValid) {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.OK.value(), "user verified"));
            } else {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "user not verified"));
            }
        } else {
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
        }
    }


//    @Override
//    public ResponseEntity<?> verifyTopt(MfaVerificationRequest mfaVerificationRequest) {
//        Optional<User> user = userRepository.findByNickname(mfaVerificationRequest.getNickname());
//        if(user.isEmpty()) {
//            return ResponseEntity.ok("user not found");
//        }
//        User user1 = user.get();
//        boolean isCodeValid = totpManager.verifyTotp(mfaVerificationRequest.getCode(), user1.getSecretKey());
//        if (isCodeValid) {
//            return ResponseEntity.ok("ok");
//        } else {
//            return ResponseEntity.ok("not ok");
//        }
//    }

    @Override
    public void createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        User user = User.builder()
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .nickname(registrationUserDto.getNickname())
                .secretKey(totpManager.generateSecretKey())
                .firstName("First name")
                .lastName("Last name")
                .build();
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("user with nickname %s not found", nickname)
                ));
        return new org.springframework.security.core.userdetails.User(
                user.getNickname(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );

    }

    @Override
    public Optional<User> getAuthenticationPrincipalUserByNickname() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        String nickname = (String) authentication.getPrincipal();
        return userRepository.findByNickname(nickname);
    }
}
