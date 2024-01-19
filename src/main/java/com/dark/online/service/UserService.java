package com.dark.online.service;

import com.dark.online.dto.security.RegistrationUserDto;
import com.dark.online.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    ResponseEntity<?> registerUserByQrCode(RegistrationUserDto registrationUserDto);
    boolean verifyTotp(String code, String username);
    ResponseEntity<?> sendRegistrationConfirmationEmail(User user);
    boolean verifyUser(String token);
}
