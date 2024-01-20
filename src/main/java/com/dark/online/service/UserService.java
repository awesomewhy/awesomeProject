package com.dark.online.service;

import com.dark.online.dto.mfa.MfaVerificationRequest;
import com.dark.online.dto.product.CreateOrderDto;
import com.dark.online.dto.security.LoginRequestDto;
import com.dark.online.dto.user.RegistrationUserDto;
import com.dark.online.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface UserService extends UserDetailsService{
    Optional<User> getAuthenticationPrincipalUserByNickname();
    void createNewUser(@RequestBody RegistrationUserDto registrationUserDto);
    ResponseEntity<?> createOrder(@RequestBody CreateOrderDto createOrderDto);
    UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException;

}
