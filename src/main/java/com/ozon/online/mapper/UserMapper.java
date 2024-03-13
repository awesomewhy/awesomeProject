package com.ozon.online.mapper;

import com.ozon.online.dto.user.RegistrationUserDto;
import com.ozon.online.entity.Role;
import com.ozon.online.entity.User;
import com.ozon.online.repository.UserRepository;
import com.ozon.online.service.TotpManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final TotpManagerService totpManagerService;
    private final UserRepository userRepository;

    public void mapRegistrationUserDtoToUserEntityAndSave(RegistrationUserDto registrationUserDto, Role role) {
        User user = User.builder()
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .nickname(registrationUserDto.getNickname())
                .secretKey(totpManagerService.generateSecretKey())
                .balance(BigDecimal.ZERO)
                .username("First name")
                .surname("Last name")
                .createdAt(LocalDateTime.now())
                .roles(List.of(role))
                .build();
        userRepository.save(user);
    }
}