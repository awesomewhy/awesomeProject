package com.ozon.online.service;

import com.ozon.online.dto.order.CreateOrderDto;
import com.ozon.online.dto.user.RegistrationUserDto;
import com.ozon.online.entity.User;
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
