package com.dark.online.service.impl;

import com.dark.online.dto.mfa.MfaTokenData;
import com.dark.online.dto.security.RegistrationUserDto;
import com.dark.online.entity.EmailConfirmationToken;
import com.dark.online.entity.User;
import com.dark.online.exception.ErrorResponse;
import com.dark.online.repository.EmailConfirmationTokenRepository;
import com.dark.online.repository.UserRepository;
import com.dark.online.service.EmailService;
import com.dark.online.service.TotpManagerService;
import com.dark.online.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl
        implements UserService {
    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpManagerService totpManager;
    private final EmailService emailService;

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    @Override
    public ResponseEntity<?> registerUserByQrCode(RegistrationUserDto registrationUserDto) {
        try {
            Optional<User> u = userRepository.findByEmail(registrationUserDto.getEmail());
            if (u.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse
                        (HttpStatus.CONFLICT, "user already exists"));
            }
            registrationUserDto.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
            User user = createNewUser(registrationUserDto);
            user.setSecretKey(totpManager.generateSecretKey());
            User savedUser = userRepository.save(user);
            if (this.sendRegistrationConfirmationEmail(user).getBody() != HttpStatus.OK) {
                return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "56 line problems with sending to email some info"));
            }
            String qrCode = totpManager.getQRCode(savedUser.getSecretKey());
            return ResponseEntity.ok().body(
                    MfaTokenData.builder()
                            .mfaCode(savedUser.getSecretKey())
                            .qrCode(qrCode)
                            .build());
        } catch (QrGenerationException e) {
            return ResponseEntity.ok().body(new ErrorResponse
                    (HttpStatus.BAD_REQUEST, "QrGenerationException in user service 63 line code"));
        }

    }

    public User createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        User user = User.builder()
                .email(registrationUserDto.getEmail())
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .nickname(registrationUserDto.getNickname())
                .firstName("First name")
                .lastName("Last name")
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean verifyTotp(String code, String username) {
        User user = userRepository.findByEmail(username).get();
        return totpManager.verifyTotp(code, user.getSecretKey());
    }

    @Override
    public ResponseEntity<?> sendRegistrationConfirmationEmail(User user) {
        try {
//            String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
            EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
//            emailConfirmationToken.setToken(tokenValue);
            emailConfirmationToken.setTimeStamp(LocalDateTime.now());
            emailConfirmationToken.setUser(user);
            emailConfirmationTokenRepository.save(emailConfirmationToken);
            emailService.sendConfirmationEmail(emailConfirmationToken);
            return ResponseEntity.ok().body(HttpStatus.OK);
        } catch (MessagingException e) {
            log.debug("ky 88 line");
            return ResponseEntity.ok().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "89 line user service"));
        }
    }

    @Override
    public boolean verifyUser(String token) {
        return false;
    }

//    @Override
//    public boolean verifyUser(String token) {
//        Optional<EmailConfirmationToken> emailConfirmationToken = emailConfirmationTokenRepository.findByToken(token);
//        if (Objects.isNull(emailConfirmationToken) || !token.equals(emailConfirmationToken.getToken())) {
//
//        }
//        if(emailConfirmationToken.isEmpty() || )
//        User user = emailConfirmationToken.getUser();
//        if (Objects.isNull(user)) {
//            return false;
//        }
//        user.setAccountVerified(true);
//        userRepository.save(user);
//        emailConfirmationTokenRepository.delete(emailConfirmationToken);
//        return true;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email)
//                ));
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
//        );
//    }
}
