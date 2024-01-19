package com.dark.online.service;

import com.dark.online.dto.MfaTokenData;
import com.dark.online.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    ResponseEntity<?> registerUser(User user);
    boolean verifyTotp(String code, String username);
    ResponseEntity<?> sendRegistrationConfirmationEmail(User user);
    boolean verifyUser(String token);
}
